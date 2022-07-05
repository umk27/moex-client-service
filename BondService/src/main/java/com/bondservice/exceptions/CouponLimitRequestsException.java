package com.bondservice.exceptions;

public class CouponLimitRequestsException extends RuntimeException{
    public CouponLimitRequestsException(String message) {
        super(message);
    }
}
