package com.devsouzx.adotapet.infra.redis;

import com.devsouzx.adotapet.properties.RedisProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@AllArgsConstructor
public class RedisConfig {
    private final RedisProperties properties;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
        if (properties.getPassword() != null && !properties.getPassword().isBlank()) {
            config.setPassword(RedisPassword.of(properties.getPassword()));
        }
        return new JedisConnectionFactory(config);
    }
}
