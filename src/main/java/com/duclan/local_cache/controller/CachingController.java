package com.duclan.local_cache.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duclan.local_cache.service.AsyncLoadingCacheService;
import com.duclan.local_cache.service.CacheService;
import com.duclan.local_cache.service.LoadingCacheService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CachingController {
    final CacheService service;
    final LoadingCacheService servicerSync;
    final AsyncLoadingCacheService serviceAsync;

    @GetMapping("/{key}")
    String getData(@PathVariable String key) {
        return service.getData(key);
    }

    @GetMapping("/sync/{key}")
    String getDataSync(@PathVariable String key) throws InterruptedException {
        return servicerSync.getData(key);
    }

    @GetMapping("/async/{key}")
    CompletableFuture<String> getDataAsync(@PathVariable String key) throws InterruptedException {
        return serviceAsync.getData(key);
    }
}
