package com.duclan.local_cache.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@Slf4j

public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache("cacheA", caffeineCacheBuilder());
        cacheManager.registerCustomCache("cacheB", loadingCaffeineCacheBuilder());
        cacheManager.registerCustomCache("cacheC", asyncLoadingCache());

        return cacheManager;
    }

    Cache<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // Time to live = 5 minutes
                .maximumSize(3) // Max Entity = 100
                .recordStats().build(); // Ghi lại thông kê về cache (lưu ý: chỉ dể mục đích kiểm tra và giám sát)

    }

    LoadingCache<Object, Object> loadingCaffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(k -> {
                    return "Data generated for " + k;
                });
    }

    AsyncLoadingCache<Object, Object> asyncLoadingCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(k -> {
                    return CompletableFuture.supplyAsync(() -> "Data generated for " + k);
                });

    }

}
