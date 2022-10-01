package com.userinterface.configuration;

import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic shareTopic() {
        return TopicBuilder.name("share_request").build();
    }

    @Bean
    public NewTopic bondTopic() {
        return TopicBuilder.name("bond_request").build();
    }

    @Bean
    public Gson jsonConverter(){
        return new Gson();
    }
}
