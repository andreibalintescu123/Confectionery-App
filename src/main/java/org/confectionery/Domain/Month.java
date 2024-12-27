package org.confectionery.Domain;
/**
 *  Enum representing all months, from January - December
 */
public enum Month {
    January, February, March, April, May, June, July, August, September, October, November, December;

    public static Month of(int monthInput) {
        switch (monthInput) {
            case 1:
                return Month.January;
            case 2:
                return Month.February;
            case 3:
                return Month.March;
            case 4:
                return Month.April;
            case 5:
                return Month.May;
            case 6:
                return Month.June;
            case 7:
                return Month.July;
            case 8:
                return Month.August;
            case 9:
                return Month.September;
            case 10:
                return Month.October;
            case 11:
                return Month.November;
            case 12:
                return Month.December;


        }
        return null;
    }
}