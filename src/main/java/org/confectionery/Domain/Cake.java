package org.confectionery.Domain;

import java.io.Serializable;

/**
 * This class represents one of our main products.
 * It includes info about its calories count.
 */
public class Cake extends Product implements Serializable {

    private final int calories;
    public Cake(int idProduct, String name, double price, double weight, ExpirationDate expirationDate,int points, int calories) {
        super(idProduct, name, price, weight, expirationDate,points);
        this.calories=calories;
    }


    /**
     * gets the calories from the Cake
     */
    public int getCalories() {
        return calories;
    }

    /**
     *
     * @return a String method containing the Cake id,name,price,weight,expirationDate,points and calories
     */
    @Override
    public String toString() {
        return getID() + ". " + getName() + ", " + getWeight() + " gr" + ", " + getCalories() + " calories" + "..............  " + getPrice() + "lei" + '\n' +
                "Expires on the " + getExpirationDate().getDay() + " of " + getExpirationDate().getMonth() + " " + getExpirationDate().getYear() + '\n' +
                "Points: " + getPoints();
    }


}
