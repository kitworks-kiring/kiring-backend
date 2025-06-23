package io.dodn.springboot.redis.repository;

import java.util.Optional;
import java.util.Set;

public interface RecommendationCacheRepository {
    // 오늘의 추천 멤버 ID 조회
    Optional<String> findTodaysRecommendation(Long userId);

    // 오늘의 추천 멤버 ID 저장 (24시간)
    void saveTodaysRecommendation(Long userId, String recommendedMemberId);

    // 최근 30일 추천 히스토리 조회
    Set<String> findRecentRecommendations(Long userId);

    // 최근 30일 추천 히스토리에 추가
    void saveToRecentRecommendations(Long userId, String recommendedMemberId);

    void clearHistory(Long userId);

}
