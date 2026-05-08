package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.TaxDAO;
import com.m3.flooringMastery.dao.TaxPersistenceException;
import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.util.List;

public class TaxDAOSTUBImpl implements TaxDAO {

    @Override
    public BigDecimal getTaxRate(String state) {

        if(state.equalsIgnoreCase("CA")) {
            return new BigDecimal("25.00");
        }

        return null;
    }

    @Override
    public boolean stateExists(String stateAbbreviation) throws TaxPersistenceException {
        return false;
    }

    @Override
    public List<Tax> getAllTaxes() throws TaxPersistenceException {
        return List.of();
    }
}