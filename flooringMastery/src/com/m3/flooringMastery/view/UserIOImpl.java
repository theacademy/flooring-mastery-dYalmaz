package com.m3.flooringMastery.view;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.math.BigDecimal;

/**
 * UserIOImpl - Handles all user input and output operations.
 * This class provides methods for reading and validating user input (strings, integers, BigDecimals, dates)
 * and displaying messages to the console. It serves as the interface between the user and the application.
 */
public class UserIOImpl implements UserIO{

private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("M/d/yyyy");

private final Scanner sc = new Scanner(System.in);


    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt) {

        while (true) {
            try {
                System.out.println(prompt);
                return Integer.parseInt(sc.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from the menu.");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {

        while (true) {
            try {
                System.out.println(prompt);
                int value = Integer.parseInt(sc.nextLine().trim());

                if (value >= min && value <= max) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // ignore
            }

            System.out.println("Invalid choice. Enter a number between " + min + " and " + max);
        }
    }

    @Override
    public java.math.BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return new BigDecimal(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    @Override
    public java.math.BigDecimal readBigDecimal(String prompt, java.math.BigDecimal min) {
        while (true) {
            try {
                System.out.println(prompt);
                BigDecimal userBD = new BigDecimal(sc.nextLine().trim());

                if (userBD.compareTo(min) >= 0) {
                    return userBD;
                }
            } catch (NumberFormatException e) {
                // keep looping below
            }

            System.out.println("Invalid number. Please enter a value of at least " + min + ".");
        }
    }



    @Override
    public LocalDate readDate(String prompt) {

        while (true) {
            try {
                System.out.println(prompt);
                String userDate = sc.nextLine();

                return LocalDate.parse(userDate, DATE_FORMATTER);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please use M/d/yyyy (e.g. 6/1/2013)");
            }
        }
    }

}
