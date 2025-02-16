package com.duclan.local_cache.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoadingCacheService {
    private final CacheManager cacheManager;

    // Constructor inject CacheManager
    public LoadingCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // Lấy dữ liệu từ cache
    public String getData(String key) throws InterruptedException {
        Cache cache = cacheManager.getCache("cacheB"); // Lấy cache từ CacheManager
        String data = null;
        log.info("Caching...");

        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key); // Kiểm tra nếu có dữ liệu trong cache
            if (valueWrapper != null) {
                data = (String) valueWrapper.get(); // Lấy giá trị từ cache
                log.info("Cache hit for key: {}", key);
            } else {

                log.info("Cache miss for key: {}", key);
            }
        }

        // Nếu không có trong cache, trả về dữ liệu mới và lưu vào cache
        if (data == null) {
            log.info("Cache miss for key: {}", key);
            data = "Data for " + key; // Dữ liệu mới
            // Lưu vào cache
            if (cache != null) {
                cache.put(key, data); // Lưu dữ liệu vào cache
            }
        }

        Thread.sleep(5000); // Giả lập load dữ liệu mất 2 giây
        log.info("Caching...Sync");

        return data;
    }

    // Cập nhật cache với dữ liệu mới
    public String updateCache(String key) {
        Cache cache = cacheManager.getCache("cacheB"); // Lấy cache từ CacheManager
        String newData = "Caching new key: " + key;

        if (cache != null) {
            cache.put(key, newData); // Cập nhật cache
        }

        log.info("Updated cache for key: {}", key);
        return newData;
    }
}
