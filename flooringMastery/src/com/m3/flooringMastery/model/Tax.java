package com.m3.flooringMastery.model;


import java.math.BigDecimal;

/**
 * Tax - Model class representing tax information for a state.
 * This class holds state-specific tax data including state abbreviation, full state name, and tax rate.
 * It provides getters and setters for managing tax information per state.
 */
public class Tax {
    String stateAbbreviation;
    String stateName;
    BigDecimal taxRate;

    public Tax(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

}
