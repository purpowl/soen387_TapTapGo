package com.taptapgo.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("Product not found in warehouse.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}