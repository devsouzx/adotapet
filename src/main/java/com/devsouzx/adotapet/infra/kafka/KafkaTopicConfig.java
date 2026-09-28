package com.devsouzx.adotapet.infra.kafka;

import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic passwordResetTopic() {
        return TopicBuilder
                .name("adotapet-password-reset")
                .build();
    }

    @Bean
    public NewTopic abrigoResetPassword() {
        return TopicBuilder
                .name("abrigo-reset-password")
                .build();
    }
}
