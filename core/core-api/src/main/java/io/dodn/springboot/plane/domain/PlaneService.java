package io.dodn.springboot.plane.domain;

import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.plane.controller.request.ReadMessageRequest;
import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.plane.PlaneRepository;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneService {
    private final PlaneRepository planeRepository;
    private final MemberRepository memberRepository;

    public PlaneService(final PlaneRepository planeRepository, final MemberRepository memberRepository) {
        this.planeRepository = planeRepository;
        this.memberRepository = memberRepository;
    }

    public Plane sendMessage(final @Valid SendMessageRequest request) {
        final Member sender = memberRepository.findById(request.senderId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_MEMBER, "Member not found with id: " + request.senderId()));

        final Member receiver = memberRepository.findById(request.receiverId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_MEMBER, "Member not found with id: " + request.receiverId()));


        // 실제로는 자기 자신에게 쪽지를 보내는 등의 비즈니스 규칙 검사도 추가될 수 있음
        if (sender.getId().equals(receiver.getId())) {
            // 예외 처리 또는 다른 로직 (여기서는 간단히 예외로 가정)
            throw new CoreException(ErrorType.DEFAULT_ERROR, "자기 자신에게 쪽지를 보낼 수 없습니다.");
        }

        final Plane plane = Plane.create(
                sender,
                receiver,
                request.message()
        );
        return planeRepository.save(plane);
    }


    public void readMessage(final @Valid ReadMessageRequest request) {
        // 쪽지 읽기 로직 구현
        final List<Plane> planeList = planeRepository.findByReceiverId(request.readerId());
        planeList.forEach(Plane::markAsRead);
    }
}
