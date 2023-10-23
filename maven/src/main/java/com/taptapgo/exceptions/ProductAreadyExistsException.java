package com.taptapgo.exceptions;

public class ProductAreadyExistsException extends Exception{
    public ProductAreadyExistsException() {
        super("A product with the same SKU already exists.");
    }
    public ProductAreadyExistsException(String message) {
        super(message);
    }
}
