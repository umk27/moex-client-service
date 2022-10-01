package com.shareservice.kafka;

import com.shareservice.model.Share;
import com.shareservice.services.ShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final ShareService shareService;

    private final Producer producer;

    public Consumer(ShareService shareService, Producer producer) {
        this.shareService = shareService;
        this.producer = producer;
    }

    @KafkaListener(topics = "share_request",
            groupId = "share-service")
    public void consume(String secid) {
        logger.info("Запрос получен");
        Share share = shareService.getShare(secid);
        producer.sendMessage(share);
        logger.info("Результат отправлен");
    }

}
