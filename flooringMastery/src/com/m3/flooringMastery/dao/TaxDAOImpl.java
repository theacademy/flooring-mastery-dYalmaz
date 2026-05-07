package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Tax;
import com.m3.flooringMastery.service.TaxPersistenceException;

import java.math.BigDecimal;

public class TaxDAOImpl implements TaxDAO {

     String TAX_FILE;
     Tax tax;

     public TaxDAOImpl(String TAX_FILE) {
         this.TAX_FILE = TAX_FILE;
     }

    @Override
    public BigDecimal getTaxRate(String stateAbbreviation) throws TaxPersistenceException {
        return null;
    }

    @Override
    public boolean stateExists(String stateAbbreviation) throws TaxPersistenceException {
        return false;
    }

    @Override
    public void getAllTaxes() throws TaxPersistenceException {

    }
}
