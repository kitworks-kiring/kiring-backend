package io.dodn.springboot.matzip.domain.event;

public record LikeToggledEvent(
        Long memberId,
        Long placeId,
        boolean isLikeAction
) {
}
