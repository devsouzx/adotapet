package com.devsouzx.adotapet.service.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(StringRedisTemplate redisTemplate,
                        ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void removeKey(String key) {
        redisTemplate.delete(key);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        try {
            return objectMapper.readValue(value, clazz);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao desserializar valor do Redis",
                    e
            );
        }
    }

    public void setValue(String key,
                         Object value,
                         TimeUnit unit,
                         long timeout) {

        try {
            String json = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue()
                    .set(key, json, timeout, unit);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao serializar valor para o Redis",
                    e
            );
        }
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}