package com.userinterface.kafka;

import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShareServiceProducer {

    private final NewTopic shareTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ShareServiceProducer(NewTopic shareTopic, KafkaTemplate<String, String> kafkaTemplate) {
        this.shareTopic = shareTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String secid) {
        System.out.println(shareTopic.name());
        kafkaTemplate.send(shareTopic.name(), secid);
    }
}
