package com.devsouzx.adotapet.infra.config.kafka;

import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    public NewTopic passwordResetTopic() {
        return TopicBuilder
                .name("adotapet-password-reset")
                .build();
    }

    public NewTopic abrigoResetPassword() {
        return TopicBuilder
                .name("abrigo-reset-password")
                .build();
    }
}
