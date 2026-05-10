package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.TaxDAO;
import com.m3.flooringMastery.dao.TaxPersistenceException;
import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.util.List;

public class TaxDAOSTUBImpl implements TaxDAO {

    @Override
    public BigDecimal getTaxRate(String state) {
        if (state == null) return null;
        if (state.equalsIgnoreCase("CA")) {
            return new BigDecimal("8.25");
        }

        return null;
    }

    @Override
    public boolean stateExists(String stateAbbreviation) throws TaxPersistenceException {
        if (stateAbbreviation == null) return false;
        return stateAbbreviation.equalsIgnoreCase("CA");
    }

    @Override
    public List<Tax> getAllTaxes() throws TaxPersistenceException {
        Tax ca = new Tax("CA");
        ca.setTaxRate(new BigDecimal("8.25"));
        return List.of(ca);
    }
}