package com.bondservice.kafka;

import com.bondservice.model.Bond;
import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private final NewTopic topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Gson jsonConverter;

    public Producer(NewTopic topic, KafkaTemplate<String, String> kafkaTemplate, Gson jsonConverter) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.jsonConverter = jsonConverter;
    }

    public void sendMessage(Bond bond) {
        kafkaTemplate.send(topic.name(), jsonConverter.toJson(bond));
    }
}
