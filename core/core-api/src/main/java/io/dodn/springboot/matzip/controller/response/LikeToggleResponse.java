package io.dodn.springboot.matzip.controller.response;

public record LikeToggleResponse(
        boolean isLiked, // 토글 후의 좋아요 상태
        long likeCount   // 토글 후의 총 좋아요 개수
) {
}
