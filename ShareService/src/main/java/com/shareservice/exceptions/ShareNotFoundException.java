package com.shareservice.exceptions;

public class ShareNotFoundException extends RuntimeException{
    public ShareNotFoundException(String message) {
        super(message);
    }
}
