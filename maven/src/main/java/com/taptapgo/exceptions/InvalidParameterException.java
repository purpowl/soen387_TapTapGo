package com.taptapgo.exceptions;

public class InvalidParameterException extends Exception {

    public InvalidParameterException() {
        super("Invalid parameter supplied.");
    }
    public InvalidParameterException(String message) {
        super(message);
    }
}