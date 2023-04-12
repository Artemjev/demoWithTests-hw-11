package com.example.demowithtests.util.exception;

public class InvalidFileFormatException extends RuntimeException{
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
