package com.taptapgo.exceptions;

public class DatabaseException extends Exception{
    public DatabaseException() {
        super("Database operations failed.");
    }

    public DatabaseException(String message) {
        super(message);
    }
}
