package io.dodn.springboot.storage.db.matzip.repository;

import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlaceRepositoryCustom {
    Page<PlaceNearbyDto> findNearbyPlaces(
            double latitude,
            double longitude,
            int radius,
            Long categoryId, // 카테고리 필터 파라미터 추가
            Pageable pageable
    );

    Map<Long, List<String>> findCategoryNamesMapByPlaceIds(List<Long> placeIds);

}
