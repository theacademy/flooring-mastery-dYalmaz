package com.m3.flooringMastery.dao;

/**
 * OrderPersistenceException - Exception thrown when order file I/O operations fail.
 * Indicates errors while reading from, writing to, or manipulating order data files.
 */

public class OrderPersistenceException extends Exception {
    public OrderPersistenceException(String message) {
        super(message);
    }

    public OrderPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
