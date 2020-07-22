package com.grouppage.exception;

public class WrongDataPostedException extends RuntimeException {
    public WrongDataPostedException(String message) {
        super(message);
    }
}
