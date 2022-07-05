package com.shareservice.exceptions;

public class ShareLimitsRequestsException extends RuntimeException{
    public ShareLimitsRequestsException(String message) {
        super(message);
    }
}
