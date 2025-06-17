package io.dodn.springboot.matzip.controller.response;

import io.dodn.springboot.storage.db.matzip.PlaceWithDistance;

public record NearbyPlaceResponse(
        Long placeId,
        String name,
        String address,
        String phoneNumber,
        String description,
        int likeCount,
        double latitude,
        double longitude,
        Double distanceInMeters // ★ 내 위치로부터의 거리 (미터)
) {
    public static NearbyPlaceResponse fromProjection(PlaceWithDistance place) {
        return new NearbyPlaceResponse(
                place.getPlaceId(),
                place.getName(),
                place.getAddress(),
                place.getPhoneNumber(),
                place.getDescription(),
                place.getLikeCount(),
                place.getLatitude(),
                place.getLongitude(),
                place.getDistance()
        );

    }
}
