package com.duclan.local_cache.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.springframework.stereotype.Service;

import com.duclan.local_cache.entity.DataObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    public String getData(String key) {
        Cache cache = cacheManager.getCache("cacheA");

        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                log.info("Cache hit");
                return "Data for key: " + key + "\nfrom cache: " + valueWrapper.get();
            }
            log.info("Cache miss for: " + key);
        }

        // Đoạn code để lấy dữ liệu từ nguồn nào đó, ví dụ từ database hoặc external API
        String data = "Data for key: " + key;

        // Cache dữ liệu
        if (cache != null) {
            cache.put(key, data);
        }

        return data;
    }
}