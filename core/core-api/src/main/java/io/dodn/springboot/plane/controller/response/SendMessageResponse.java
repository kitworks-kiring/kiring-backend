package io.dodn.springboot.plane.controller.response;

import io.dodn.springboot.storage.db.plane.entity.Plane;

import java.time.LocalDateTime;

public record SendMessageResponse(
        Long messageId,
        Long senderId,
        String senderName,
        Long receiverId,
        String receiverName,
        String message,
        LocalDateTime sentAt
) {
    public static SendMessageResponse fromEntity(Plane message) {
        return new SendMessageResponse(
                message.getId(),
                message.getSender().getId(), // User 엔티티에 getUserId()가 있다고 가정
                message.getSender().getName(),   // User 엔티티에 getName()이 있다고 가정
                message.getReceiver().getId(),
                message.getReceiver().getName(),
                message.getMessage(),
                message.getCreatedAt()
        );
    }
}
