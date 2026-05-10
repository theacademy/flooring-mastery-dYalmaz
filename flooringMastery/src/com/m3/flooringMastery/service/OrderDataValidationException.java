package com.m3.flooringMastery.service;

/**
 * OrderDataValidationException - Exception thrown when order data validation fails.
 * Indicates that provided order data does not meet business validation rules.
 */

public class OrderDataValidationException extends RuntimeException {
    public OrderDataValidationException(String message) {
        super(message);
    }
}
