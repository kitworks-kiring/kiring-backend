package io.dodn.springboot.matzip.controller.response;

import io.dodn.springboot.storage.db.matzip.entity.Place;

import java.math.BigDecimal;

public record PlaceResponse(
        Long placeId,
        String name,
        String address,
        String phoneNumber,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        long likeCount, // 맛집의 총 좋아요 수
        boolean isLiked // ★ 현재 로그인한 사용자의 좋아요 여부
) {
    /**
     * Place 엔티티와 좋아요 여부(isLiked)를 받아서 PlaceResponse 레코드를 생성하는 정적 팩토리 메서드
     * @param place Place 엔티티
     * @param isLiked 현재 사용자의 좋아요 여부
     * @return PlaceResponse 레코드 인스턴스
     */
    public static PlaceResponse of(Place place, boolean isLiked) {
        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getPhoneNumber(),
                place.getDescription(),
                place.getLatitude(),
                place.getLongitude(),
                place.getLikeCount(), // Place 엔티티에 getLikeCount() 메서드가 있다고 가정
                isLiked
        );
    }

}
