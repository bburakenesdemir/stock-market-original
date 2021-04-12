package com.burakenesdemir.stockmarket.data;

import com.burakenesdemir.stockmarket.resource.TweetResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RedisRepository {

    private final HashOperations<String, String, TweetResource> hashOperations;

    public RedisRepository(RedisTemplate<String, TweetResource> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(Map<String, TweetResource> map, String hashtag) {
        hashOperations.putAll(hashtag, map);
    }

    public List<TweetResource> findAll(String hashtag) {
        return hashOperations.values(hashtag);
    }

    public TweetResource findById(String id, String hashtag) {
        return hashOperations.get(hashtag, id);
    }

    public void deleteAll(String hashtag) {
        hashOperations.getOperations().delete(hashtag);
    }
}