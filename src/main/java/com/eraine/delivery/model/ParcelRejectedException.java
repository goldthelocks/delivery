package com.eraine.delivery.model;

public class ParcelRejectedException extends RuntimeException {
    public ParcelRejectedException(String message) {
        super(message);
    }

    public ParcelRejectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
