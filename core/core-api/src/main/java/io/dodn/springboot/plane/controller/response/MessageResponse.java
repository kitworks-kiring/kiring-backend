package io.dodn.springboot.plane.controller.response;

import io.dodn.springboot.plane.domain.PlaneInfo;

import java.time.LocalDateTime;

public record  MessageResponse(
        Long messageId,
        Long receiverId,
        SenderResponse sender,
        String message,
        LocalDateTime sentAt
) {

    public static MessageResponse fromEntity(PlaneInfo planeInfo) {
        return new MessageResponse(
                planeInfo.messageId(),
                planeInfo.receiverId(),
                planeInfo.sender(),
                planeInfo.message(),
                planeInfo.sentAt()
        );
    }
}
