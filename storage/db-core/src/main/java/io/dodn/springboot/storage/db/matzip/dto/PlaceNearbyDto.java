package io.dodn.springboot.storage.db.matzip.dto;

public record PlaceNearbyDto(
        Long placeId,
        String name,
        String address,
        String phoneNumber,
        String imageUrl,
        String kiringCategory,
        int likeCount,
        double latitude,
        double longitude,
        Double distance
) {

}
