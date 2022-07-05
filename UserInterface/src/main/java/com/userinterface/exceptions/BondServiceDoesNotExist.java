package com.userinterface.exceptions;

public class BondServiceDoesNotExist extends RuntimeException{
    public BondServiceDoesNotExist(String message) {
        super(message);
    }
}
