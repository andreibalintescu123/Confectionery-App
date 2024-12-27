package org.confectionery.Domain;
import java.io.*;

/**
 * Class Product which implements the HasId interface.
 */
public abstract class Product implements HasID, Serializable {

    private int idProduct;
    private String name;
    private double price;
    private double weight;
    private ExpirationDate expirationDate;
    private int points;

    /**
     * @param idProduct      the product id
     * @param name           the product name
     * @param price          the product price
     * @param weight         the product weight
     * @param expirationDate the product expiration date
     * @param points         the product points
     */
    public Product(int idProduct, String name, double price, double weight, ExpirationDate expirationDate, int points) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.expirationDate = expirationDate;
        this.points = points;
    }


    /**
     * Prints the expiration date from the product.
     */
    public void viewExpirationDate() {
        System.out.print("Expiration Date: ");
        System.out.print(expirationDate.getDay() + ".");
        System.out.print(expirationDate.getMonth() + ".");
        System.out.println(expirationDate.getYear() + ".");
    }

    /**
     * Prints the price of the product.
     */
    public void printPrice() {
        System.out.println("Price: " + price);
    }

    /**
     * Prints the weight of the product.
     */
    public void printWeight() {
        System.out.println("Weight: " + weight);
    }

    /**
     * Prints the name of the product.
     */
    public void printName() {
        System.out.println("Name: " + name);
    }

    /**
     * @return the product id.
     */
    public int getIdProduct() {
        return idProduct;
    }

    /**
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @return the expiration date.
     */
    public ExpirationDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * @return the product id.
     */
    @Override
    public Integer getID() {
        return idProduct;
    }

    /**
     * @return the points from the product.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets points.
     *
     * @param points the points to set.
     */
    public void setPoints(int points) {
        this.points = points;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return a string that includes the Product id, name, price, weight, expirationDate, and points.
     */
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", expirationDate=" + expirationDate +
                ", points=" + points +
                '}';
    }

}