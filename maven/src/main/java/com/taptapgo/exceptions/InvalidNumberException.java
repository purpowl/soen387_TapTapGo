package com.taptapgo.exceptions;

public class InvalidNumberException extends Exception{
    
    public InvalidNumberException() {
        super("Invalid number entered");
    }

    public InvalidNumberException(String message) {
        super(message);
    }
}
