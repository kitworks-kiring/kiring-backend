package io.dodn.springboot.matzip.controller.response;

import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record NearbyPlaceResponse(
        Long placeId,
        String name,
        String address,
        String phoneNumber,
        String imageUrl,
        List<String> kiringCategory,
        List<String> categories,
        boolean isLiked, // 현재 로그인한 사용자의 좋아요 여부
        int likeCount,
        double latitude,
        double longitude,
        Double distanceInMeters // ★ 내 위치로부터의 거리 (미터)
) {
    public static NearbyPlaceResponse from(PlaceNearbyDto dto, Map<Long, List<String>> categoriesMap, boolean isLiked) {
        List<String> categoryNames = categoriesMap.getOrDefault(dto.placeId(), Collections.emptyList());

        return new NearbyPlaceResponse(
                dto.placeId(),
                dto.name(),
                dto.address(),
                dto.phoneNumber(),
                dto.imageUrl(),
                List.of(dto.kiringCategory().trim().split(",")),
                categoryNames,
                isLiked,
                dto.likeCount(),
                dto.latitude(),
                dto.longitude(),
                dto.distance());
    }
}
