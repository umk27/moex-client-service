package com.userinterface.kafka;

import com.google.gson.Gson;
import com.userinterface.controller.BondController;
import com.userinterface.model.Bond;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BondServiceConsumer {

    private final BondController bondController;

    private final Gson jsonConverter;

    public BondServiceConsumer(BondController bondController, Gson jsonConverter) {
        this.bondController = bondController;
        this.jsonConverter = jsonConverter;
    }

    @KafkaListener(topics = "bond_response",
            groupId = "user-bond")
    public void consume(String message) {
        Bond bond = (Bond) jsonConverter.fromJson(message, Bond.class);
        bondController.setBond(bond);
    }
}
