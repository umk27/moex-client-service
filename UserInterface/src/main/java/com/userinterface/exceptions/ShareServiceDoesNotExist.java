package com.userinterface.exceptions;

public class ShareServiceDoesNotExist extends RuntimeException{
    public ShareServiceDoesNotExist(String message) {
        super(message);
    }
}
