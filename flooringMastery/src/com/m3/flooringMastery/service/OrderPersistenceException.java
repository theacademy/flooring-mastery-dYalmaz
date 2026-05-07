package com.m3.flooringMastery.service;

public class OrderPersistenceException extends Exception {
    public OrderPersistenceException(String message) {
        super(message);
    }

    public OrderPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
