package com.userinterface.kafka;

import com.google.gson.Gson;
import com.userinterface.controller.ShareController;
import com.userinterface.model.Share;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShareServiceConsumer {

    private final ShareController shareController;

    private final Gson jsonConverter;

    public ShareServiceConsumer(ShareController shareController, Gson jsonConverter) {
        this.shareController = shareController;
        this.jsonConverter = jsonConverter;
    }

    @KafkaListener(topics = "share_response",
            groupId = "user-share")
    public void consume(String message) {
      Share share = (Share) jsonConverter.fromJson(message, Share.class);
      shareController.setShare(share);
    }
}
