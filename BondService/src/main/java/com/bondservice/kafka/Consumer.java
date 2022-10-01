package com.bondservice.kafka;

import com.bondservice.model.Bond;
import com.bondservice.services.BondService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final BondService bondService;

    private final Producer producer;

    public Consumer(BondService bondService, Producer producer) {
        this.bondService = bondService;
        this.producer = producer;
    }

    @KafkaListener(topics = "bond_request",
            groupId = "bond-service")
    public void consume(String secid) {
        logger.info("Запрос получен");
        Bond bond = bondService.getBond(secid);
        producer.sendMessage(bond);
        logger.info("Результат отправлен");
    }
}
