package org.confectionery.Domain;
import java.io.*;

/**
 * Class Product which implements the HasId interface.
 */
public abstract class Product implements HasID, Serializable {


    private final int productID;
    private String name;
    private double price;
    private double weight;
    private final ExpirationDate expirationDate;
    private int points;

    /**
     * @param name           the product name
     * @param price          the product price
     * @param weight         the product weight
     * @param expirationDate the product expiration date
     * @param points         the product points
     */
    public Product( String name, double price, double weight, ExpirationDate expirationDate, int points) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.expirationDate = expirationDate;
        this.points = points;
        this.productID = IDGenerator.getInstance().generateID();
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
        return productID;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    /**
     * @return a string that includes the Product id, name, price, weight, expirationDate, and points.
     */
    public String toString() {
        return "Product{" +
                "idProduct=" + productID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", expirationDate=" + expirationDate +
                ", points=" + points +
                '}';
    }

}