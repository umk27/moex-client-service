package com.bondservice.exceptions;

public class GovBondLimitRequestsException extends RuntimeException{
    public GovBondLimitRequestsException(String message) {
        super(message);
    }
}
