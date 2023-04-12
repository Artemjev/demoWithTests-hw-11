package com.example.demowithtests.util.exception;

public class InvalidFileSizeException extends RuntimeException{
    public InvalidFileSizeException(String message) {
        super(message);
    }
}
