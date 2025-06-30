package io.dodn.springboot.matzip.domain.model;

public record LikeTask(
        Long userId,
        Long placeId,
        boolean isLike
) {
}
