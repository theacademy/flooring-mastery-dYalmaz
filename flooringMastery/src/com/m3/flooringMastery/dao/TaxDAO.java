package com.m3.flooringMastery.dao;


import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.util.List;

/**
 * TaxDAO - Interface defining contract for tax data access operations.
 * Specifies methods for retrieving tax rates by state and validating state availability.
 */
public interface TaxDAO {

    BigDecimal getTaxRate(String stateAbbreviation) throws TaxPersistenceException;
    boolean stateExists(String stateAbbreviation) throws TaxPersistenceException;
    List<Tax> getAllTaxes() throws TaxPersistenceException;

}
