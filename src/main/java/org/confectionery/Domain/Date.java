package org.confectionery.Domain;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an expiration date, which includes a year, month, and day.
 */
public class Date implements Serializable, Comparable<Date> {
    private final int year;
    private final Month month;
    private final Day day;

    /**
     * @param year  the year of expiration date
     * @param month the month of expiration date
     * @param day   the day of expiration date
     */
    public Date(int year, Month month, Day day) {
        this.year = year;
        this.month = month;
        this.day = day;

    }

    /**
     * @return the year of the expiration date
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the month of the expiration date
     */

    public Month getMonth() {
        return month;
    }

    /**
     * @return the day of the expiration date
     */
    public Day getDay() {
        return day;
    }

    /**
     * @return a toString Method with the ExpirationDate - year,month and day
     */
    @Override
    public String toString() {
        return year + "-" + month.name() + "-" + day.name();
    }

    /**
     * Parses a string representation of an expiration date in the format "YYYY-MONTH-DAY"
     * and returns an ExpirationDate object.
     *
     * @param dateStr the string to parse (e.g., "2024-December-Eighteenth")
     * @return an ExpirationDate object
     * @throws IllegalArgumentException if the format is invalid
     */
    public static Date parse(String dateStr) {
        try {
            String[] parts = dateStr.split("-");

            // Parse the year as an integer
            int year = Integer.parseInt(parts[0]);

            // Parse the month by matching the written name (e.g., "December")
            Month month = Month.valueOf(parts[1]);

            // Parse the day by matching the ordinal name (e.g., "Fourteenth")
            Day day = Day.valueOf(parts[2]);

            return new Date(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }

    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month.ordinal() != other.month.ordinal()) {
            return Integer.compare(this.month.ordinal(), other.month.ordinal());
        }
        return Integer.compare(this.day.ordinal(), other.day.ordinal());
    }

    /**
     * Converts a LocalDate to the custom Date class.
     *
     * @param localDate The LocalDate to convert.
     * @return The custom Date instance.
     */
    public static Date convertLocalDateToDate(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("LocalDate cannot be null");
        }

        // Extract components from LocalDate
        int year = localDate.getYear();
        Month month = Month.values()[localDate.getMonthValue() - 1]; // Convert 1-based month to 0-based enum
        Day day = Day.values()[localDate.getDayOfMonth() - 1];       // Convert 1-based day to 0-based enum

        return new Date(year, month, day);
    }

}