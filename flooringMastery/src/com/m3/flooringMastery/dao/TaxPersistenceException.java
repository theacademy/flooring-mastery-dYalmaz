package com.m3.flooringMastery.dao;

/**
 * TaxPersistenceException - Exception thrown when tax data file I/O or operations fail.
 * Indicates errors while loading or retrieving tax rate information.
 */

public class TaxPersistenceException extends Exception {

    public TaxPersistenceException(String message) {
        super(message);
    }

    public TaxPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
