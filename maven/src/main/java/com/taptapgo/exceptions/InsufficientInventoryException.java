package com.taptapgo.exceptions;

public class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException() {
        super("Not enough inventory in warehouse.");
    }

    public InsufficientInventoryException(String message) {
        super(message);
    }
}