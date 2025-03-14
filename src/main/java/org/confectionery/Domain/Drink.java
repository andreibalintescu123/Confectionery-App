package org.confectionery.Domain;

import java.io.Serializable;

/**
 * This class represents one of our main products.
 * It includes info about its alcohol percentage.
 */
public class Drink extends Product implements Serializable {

    private double alcoholPercentage;
    public Drink(String name, double price, double weight, Date expirationDate, int points, double alcohol) {
        super(name, price, weight, expirationDate,points);
        this.alcoholPercentage = alcohol;
    }

    public Drink(Integer id,String name, double price, double weight, Date expirationDate, int points, double alcohol) {
        super(id, name, price, weight, expirationDate,points);
        this.alcoholPercentage = alcohol;

    }


    /**
     * gets the alcohol from the Drinks
     */
    public  double getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(double alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    /**
     * a toString method with the attributes
     * @return the drink with the id,name,price,weight,expirationDate,points and alcohol
     */
    @Override
    public String toString() {
        return getID() + ". " + getName() + ", " + getWeight() + " ml" + ", " + getAlcoholPercentage() + "% alc" + "..............  " + getPrice() + "lei" + '\n' +
                "Expires on the " + getExpirationDate().getDay() + " of " + getExpirationDate().getMonth() + " " + getExpirationDate().getYear() + '\n' +
                "Points: " + getPoints();
    }


}