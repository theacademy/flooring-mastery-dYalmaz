package com.m3.flooringMastery.service;

/**
 * DuplicateIdException - Exception thrown when an order with a duplicate ID already exists.
 * Indicates an attempt to create or use an order ID that is not unique.
 */

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String message) {
        super(message);
    }
}
