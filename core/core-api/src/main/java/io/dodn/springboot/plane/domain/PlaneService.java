package io.dodn.springboot.plane.domain;

import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.plane.PlaneRepository;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PlaneService {
    private final PlaneRepository planeRepository;
    private final MemberRepository memberRepository;

    public PlaneService(final PlaneRepository planeRepository, final MemberRepository memberRepository) {
        this.planeRepository = planeRepository;
        this.memberRepository = memberRepository;
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
            throw new CoreException(ErrorType.ERR_1006, "같은 사람에게는 하루에 한 번만 쪽지를 보낼 수 있습니다.");
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
}
