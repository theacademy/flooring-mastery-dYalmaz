package com.m3.flooringMastery.dao;

/**
 * ProductPersistenceException - Exception thrown when product file I/O or data operations fail.
 * Indicates errors while loading or retrieving product data.
 */

public class ProductPersistenceException extends Exception {

    public ProductPersistenceException(String message) {
        super(message);
    }
}
