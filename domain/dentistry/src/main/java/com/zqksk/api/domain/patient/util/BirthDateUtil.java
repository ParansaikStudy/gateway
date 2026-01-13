package com.zqksk.api.domain.patient.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class BirthDateUtil {

    private BirthDateUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDate convertDate(String birthDate) {
        int year = Integer.parseInt(birthDate.substring(0, 2));
        int baseYear = (year >= 0 && year <= Integer.parseInt(Year.now().format(DateTimeFormatter.ofPattern("yy")))) ? 2000 : 1900;

        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .appendValueReduced(ChronoField.YEAR, 2, 2, baseYear)
                .appendPattern("MMdd")
                .toFormatter();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(birthDate, inputFormatter);
        return LocalDate.parse(date.format(outputFormatter));
    }
}
