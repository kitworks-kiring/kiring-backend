package io.dodn.springboot.matzip.controller.response;

import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.locationtech.jts.geom.Point;

import java.util.Set;

public record PlaceResponse(
        Long placeId,
        String name,
        String address,
        String phoneNumber,
        String description,
        double latitude,
        double longitude,
        long likeCount, // 맛집의 총 좋아요 수
        boolean isLiked, // 현재 로그인한 사용자의 좋아요 여부
        Set<Category> categories
) {
    /**
     * Place 엔티티와 좋아요 여부(isLiked)를 받아서 PlaceResponse 레코드를 생성하는 정적 팩토리 메서드
     * @param place Place 엔티티
     * @param isLiked 현재 사용자의 좋아요 여부
     * @return PlaceResponse 레코드 인스턴스
     */
    public static PlaceResponse of(Place place, boolean isLiked) {
        Point locationPoint = place.getLocation(); // 엔티티에서 Point 객체를 가져옴

        final double pointX = locationPoint.getX();
        final double pointY = locationPoint.getY();

        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getPhoneNumber(),
                place.getDescription(),
                pointX,
                pointY,
                place.getLikeCount(), // Place 엔티티에 getLikeCount() 메서드가 있다고 가정
                isLiked,
                place.getCategories()
        );
    }

}
