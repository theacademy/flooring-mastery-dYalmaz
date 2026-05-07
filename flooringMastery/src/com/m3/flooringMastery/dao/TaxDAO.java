package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.service.TaxPersistenceException;

import java.math.BigDecimal;

public interface TaxDAO {

    public BigDecimal getTaxRate(String stateAbbreviation) throws TaxPersistenceException;
    public boolean stateExists(String stateAbbreviation) throws TaxPersistenceException;
    public void getAllTaxes() throws TaxPersistenceException;

}
