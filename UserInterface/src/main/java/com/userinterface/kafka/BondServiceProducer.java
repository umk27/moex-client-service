package com.userinterface.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BondServiceProducer {

    private final NewTopic bondTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BondServiceProducer(NewTopic bondTopic, KafkaTemplate<String, String> kafkaTemplate) {
        this.bondTopic = bondTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String secid) {
        System.out.println(bondTopic.name());
        kafkaTemplate.send(bondTopic.name(), secid);
    }
}
