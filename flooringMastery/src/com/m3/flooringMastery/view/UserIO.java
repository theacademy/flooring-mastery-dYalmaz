package com.m3.flooringMastery.view;

/**
 * UserIO - Interface defining contract for user input and output operations.
 * Defines methods for displaying messages and reading various types of validated user input
 * (strings, integers, BigDecimals, dates).
 */

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {

    void displayMessage(String message);

    String readString(String prompt);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    BigDecimal readBigDecimal(String prompt);
    BigDecimal readBigDecimal(String prompt, BigDecimal min);

    LocalDate readDate(String s);

}
