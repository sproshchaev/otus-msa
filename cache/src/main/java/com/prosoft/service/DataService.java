package com.prosoft.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    /** Константа для TTL (время жизни кэша в секундах) */
    private static final long CACHE_TTL = 60;

    /**
     * Шаблон указывает, что ключи и значения в Redis будут иметь тип String.
     * Если бы нам требовалось использовать другие типы данных, мы могли бы создать специализированный шаблон.
     * Например, если нам нужно было бы хранить и извлекать объекты Java, мы могли бы использовать
     * RedisTemplate<String, MyCustomObject>.
     */
    private final RedisTemplate<String, String> redisTemplate;

    public DataService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getDataFromDatabase(int id) {
        String cacheKey = String.valueOf(id);
        long startTime = System.currentTimeMillis();
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            long cacheTime = System.currentTimeMillis() - startTime;
            logger.info("Данные из кэша получены для ID {} за {} мс", id, cacheTime);
            return cachedData;
        }

        startTime = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long databaseTime = System.currentTimeMillis() - startTime;
        String dbData = "Data for ID " + id;
        redisTemplate.opsForValue().set(cacheKey, dbData, CACHE_TTL, TimeUnit.SECONDS);
        logger.info("Данные из базы получены для ID {} за {} мс", id, databaseTime);
        return dbData;
    }
}
