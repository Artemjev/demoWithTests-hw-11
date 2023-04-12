package com.example.demowithtests.util.exception;

public class NoPhotoEmployeeException extends RuntimeException {
    public NoPhotoEmployeeException(String message) {
        super(message);
    }
}
