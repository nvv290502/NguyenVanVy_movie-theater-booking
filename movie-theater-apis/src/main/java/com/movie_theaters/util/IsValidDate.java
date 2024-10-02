package com.movie_theaters.util;

import java.time.DateTimeException;
import java.time.LocalDate;

public class IsValidDate {
    public static boolean isValidDate(int year, int month, int day) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
