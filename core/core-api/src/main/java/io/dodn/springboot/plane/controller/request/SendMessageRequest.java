package io.dodn.springboot.plane.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(
        @NotNull(message = "보내는 사람 ID는 필수입니다.")
        long senderId,

        @NotNull(message = "받는 사람 ID는 필수입니다.")
        long receiverId,

        @NotBlank(message = "쪽지 내용은 비워둘 수 없습니다.")
        String message
) {


}
