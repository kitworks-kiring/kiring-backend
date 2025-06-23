package io.dodn.springboot.redis.infra;

import io.dodn.springboot.redis.repository.RecommendationCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class RecommendationCacheRepositoryImpl implements RecommendationCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public RecommendationCacheRepositoryImpl(final RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getDailyKey(Long userId) {
        return "recommend:daily:" + userId;
    }

    private String getHistoryKey(Long userId) {
        return "recommend:history:" + userId;
    }

    @Override
    public Optional<String> findTodaysRecommendation(
            final Long userId
    ) {
        Object result = redisTemplate.opsForValue().get(getDailyKey(userId));
        return Optional.ofNullable((String) result);
    }

    @Override
    public void saveTodaysRecommendation(
            final Long userId,
            final String recommendedMemberId
    ) {
        redisTemplate.opsForValue().set(getDailyKey(userId), recommendedMemberId, 24, TimeUnit.HOURS);
    }

    @Override
    public Set<String> findRecentRecommendations(
            final Long userId
    ) {
        Set<Object> members = redisTemplate.opsForSet().members(getHistoryKey(userId));
        // Redis 에서 가져온 Set<Object>를 Set<String>으로 변환
        return members != null ? members.stream().map(String::valueOf).collect(Collectors.toSet()) : Set.of();
    }

    @Override
    public void saveToRecentRecommendations(
            final Long userId,
            final String recommendedMemberId
    ) {
        String key = getHistoryKey(userId);
        redisTemplate.opsForSet().add(key, recommendedMemberId);
        redisTemplate.expire(key, 30, TimeUnit.DAYS);
    }

    @Override
    public void clearHistory(final Long userId) {
        redisTemplate.delete(getHistoryKey(userId));
    }
}
