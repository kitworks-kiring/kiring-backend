package io.dodn.springboot.plane.domain;

import io.dodn.springboot.plane.controller.response.SenderResponse;
import io.dodn.springboot.storage.db.plane.entity.Plane;

import java.time.LocalDateTime;

public record PlaneInfo(
        Long messageId,
        Long receiverId,
        SenderResponse sender,
        String message,
        LocalDateTime sentAt
) {
    public static PlaneInfo fromEntity(Plane plane) {
        SenderResponse senderResponse = new SenderResponse(
                plane.getSender().getId(),
                plane.getSender().getName(),
                plane.getSender().getProfileImageUrl()
        );

        return new PlaneInfo(
                plane.getId(),
                plane.getReceiver().getId(),
                senderResponse,
                plane.getMessage(),
                plane.getCreatedAt()
        );
    }
}
