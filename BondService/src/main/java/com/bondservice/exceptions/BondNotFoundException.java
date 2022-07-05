package com.bondservice.exceptions;

public class BondNotFoundException extends RuntimeException{
    public BondNotFoundException(String s) {
        super(s);
    }
}
