package com.m3.flooringMastery.dao;

/**
 * TaxDAO - Interface defining contract for tax data access operations.
 * Specifies methods for retrieving tax rates by state and validating state availability.
 */

import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.util.List;

public interface TaxDAO {

    public BigDecimal getTaxRate(String stateAbbreviation) throws TaxPersistenceException;
    public boolean stateExists(String stateAbbreviation) throws TaxPersistenceException;
    public List<Tax> getAllTaxes() throws TaxPersistenceException;

}
