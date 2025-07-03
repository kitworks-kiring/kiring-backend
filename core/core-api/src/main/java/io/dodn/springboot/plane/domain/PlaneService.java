package io.dodn.springboot.plane.domain;

import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.redis.repository.RecommendationCacheRepository;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.plane.PlaneRepository;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaneService {
    private final PlaneRepository planeRepository;
    private final MemberRepository memberRepository;
    private final RecommendationCacheRepository recommendationCacheRepository;


    public PlaneService(final PlaneRepository planeRepository, final MemberRepository memberRepository, final RecommendationCacheRepository recommendationCacheRepository) {
        this.planeRepository = planeRepository;
        this.memberRepository = memberRepository;
        this.recommendationCacheRepository = recommendationCacheRepository;
    }

    public Plane sendMessage(
            final SendMessageRequest request
    ) {
        final Member sender = memberRepository.findById(request.senderId())
                .orElseThrow(() -> new CoreException(ErrorType.ERR_1001, "Member not found with id: " + request.senderId()));

        final Member receiver = memberRepository.findById(request.receiverId())
                .orElseThrow(() -> new CoreException(ErrorType.ERR_1001, "Member not found with id: " + request.receiverId()));


        // 자기 자신에게 쪽지를 보내는 등의 비즈니스 규칙 검사도 추가될 수 있음
        if (sender.getId().equals(receiver.getId())) {
            // 예외 처리 또는 다른 로직 (여기서는 간단히 예외로 가정)
            throw new CoreException(ErrorType.ERR_1099, "자기 자신에게 쪽지를 보낼 수 없습니다.");
        }

        // 하루에 한 번만 보내기 로직 추가
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 오늘 날짜의 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);   // 오늘 날짜의 23:59:59

        boolean alreadySentToday = planeRepository.existsBySenderAndCreatedAtBetween(sender, startOfDay, endOfDay);
        if (alreadySentToday) {
            throw new CoreException(ErrorType.ERR_1006, "하루에 한 번만 쪽지를 보낼 수 있습니다.");
        }

        final Plane plane = Plane.create(
                sender,
                receiver,
                request.message()
        );
        return planeRepository.save(plane);
    }


    @Transactional(readOnly = true)
    public List<PlaneInfo> readMessage(final long readerId) {
        final List<Plane> planeList = planeRepository.findByReceiverId(readerId);

        return planeList.stream()
                .map(PlaneInfo::fromEntity)
                .toList();
    }

    public boolean getTodayMessage(final long readerId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 오늘 날짜의 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);   // 오늘 날짜의 23:59:59

        List<Plane> planeList = planeRepository.findByReceiverIdAndCreatedAtBetween(readerId, startOfDay, endOfDay);
        return !planeList.isEmpty();
    }

    @Transactional(readOnly = true)
    public PlaneStatusInfo getPlaneStatus(final Long readerId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);


        // 보낸 쪽지의 개수
        long sentCount = planeRepository.countBySenderIdAndCreatedAtBetween(readerId, startOfDay, endOfDay);
        // 받은 쪽지 여부
        boolean receivedMessage = getTodayMessage(readerId);
        // 추천된 회원 여부
        Member recommendedMember = getTodayRecommendedMember(readerId);
        if (recommendedMember == null) {
            throw new CoreException(ErrorType.ERR_1099, "추천할 회원이 없습니다.");
        }
        return PlaneStatusInfo.of(sentCount, receivedMessage, recommendedMember);
    }

    /**
     * 오늘의 추천 멤버를 가져오는 메서드
     * @param readerId 추천을 받을 회원의 ID
     * @return 오늘의 추천 멤버, 없으면 null
     */
    private Member getTodayRecommendedMember(final Long readerId) {
        // 1. "오늘의 추천"이 캐시에 이미 있는지 확인 (가독성 향상)
        Optional<String> recommendedIdOpt = recommendationCacheRepository.findTodaysRecommendation(readerId);
        if (recommendedIdOpt.isPresent()) {
            return memberRepository.findById(Long.parseLong(recommendedIdOpt.get())).orElse(null);
        }

        // 2. 캐시에 없다면, 새로운 멤버를 추천
        Set<String> recentHistory = recommendationCacheRepository.findRecentRecommendations(readerId);

        // 2-2. DB 에서 전체 멤버 목록을 가져와 ID Set 으로 변환
        Set<String> allMemberIds = getAllMemberIds();

        allMemberIds.removeAll(recentHistory);
        allMemberIds.remove(readerId.toString());

        if (allMemberIds.isEmpty()) {
            // 30일 추천 히스토리를 깨끗하게 비웁니다.
            recommendationCacheRepository.clearHistory(readerId);
            allMemberIds = getAllMemberIds();
            allMemberIds.remove(readerId.toString());
            //    무한 루프를 방지하기 위해 null을 반환하고 종료합니다.
            if (allMemberIds.isEmpty()) {
                return null;
            }
        }

        // 2-3. 남은 후보 목록을 랜덤으로 섞어 한 명 선택
        List<String> availableList = new ArrayList<>(allMemberIds);
        Collections.shuffle(availableList);
        String newRecommendedMemberId = availableList.getFirst();

        // 3. 결과를 캐시에 저장
        recommendationCacheRepository.saveTodaysRecommendation(readerId, newRecommendedMemberId);
        recommendationCacheRepository.saveToRecentRecommendations(readerId, newRecommendedMemberId);

        return memberRepository.findById(Long.parseLong(newRecommendedMemberId)).orElse(null);
    }

    private Set<String> getAllMemberIds() {
        return memberRepository.findAll().stream()
                .map(member -> member.getId().toString())
                .collect(Collectors.toSet());
    }
}
