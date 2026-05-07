package com.m3.flooringMastery.dao;

import java.io.FileNotFoundException;

public class ProductPersistenceException extends Exception {
    public ProductPersistenceException(String s, FileNotFoundException e) {
        super(s, e);
    }
}
