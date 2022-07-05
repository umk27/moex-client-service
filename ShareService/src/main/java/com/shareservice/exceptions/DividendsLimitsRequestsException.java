package com.shareservice.exceptions;

import java.net.ConnectException;

public class DividendsLimitsRequestsException extends RuntimeException{
    public DividendsLimitsRequestsException(String message) {
        super(message);
    }
}
