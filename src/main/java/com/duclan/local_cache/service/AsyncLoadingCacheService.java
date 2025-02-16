package com.duclan.local_cache.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncLoadingCacheService {
    final CacheManager manager;

    public CompletableFuture<String> getData(String key) throws InterruptedException {
        Cache cache = manager.getCache("cacheC");
        Object cachedData = null;
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                cachedData = valueWrapper.get();
                log.info("Cache hit for key: {}", key);
            } else {
                log.info("Cache miss for key: {}", key);
            }
        }
        Thread.sleep(5000); // Giả lập load dữ liệu mất 2 giây
        log.info("Generating new data for key: {}", key);
        CompletableFuture<String> newData = CompletableFuture.completedFuture("Data for " + key);

        if (cachedData instanceof CompletableFuture) {
            return (CompletableFuture<String>) cachedData; // Trả về CompletableFuture nếu cache đã có
        }

        if (cachedData instanceof String) {
            return CompletableFuture.completedFuture((String) cachedData); // Bọc String vào CompletableFuture
        }

        log.info("Caching...Async");
        return newData;
    }

    public CompletableFuture<String> updateCache(String key) {
        Cache cache = manager.getCache("cacheC"); // Lấy cache từ CacheManager
        CompletableFuture<String> newData = CompletableFuture.completedFuture("Caching new key: " + key);

        if (cache != null) {
            cache.put(key, newData); // Cập nhật cache với CompletableFuture
        }

        log.info("Updated cache for key: {}", key);
        return newData;
    }

}
