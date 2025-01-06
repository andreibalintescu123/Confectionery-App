package org.confectionery.Domain;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an order made by a client, which contains a list of Products objects,the order ID, and the date the order was placed.
 * Provides methods to calculate the total price and total points of the order and to add products to the order.
 **/

public class Order implements HasID, Serializable {
    private final List<Product> products;
    private Integer orderID;
    private final Date date;
    private Integer clientID; // New attribute only for testing

    /**
     * @param products A list of products included in the order.
     * @param date     The date when the order was placed.
     */
    public Order(List<Product> products, Date date) {
        this.products = products;
        this.orderID = IDGenerator.getInstance().generateID();
        this.date = date;
        this.clientID = -1; // Default value (e.g., unassigned client)
    }

    public Order(Integer id,List<Product> products, Date date) {
        this.products = products;
        this.orderID = id;
        this.date = date;
        this.clientID = -1; // Default value (e.g., unassigned client)
    }
    /**
     * Calculates the total price of the order by summing the prices of all products in the order
     *
     * @return The total price of the order.
     */
    public float getTotal() {
        float totalPrice = 0;
        for (Product p : this.products) {
            totalPrice += (float) p.getPrice();
        }
        return totalPrice;
    }

    /**
     * Calculates the total points for the order by summing the points associated with all products.
     *
     * @return The total points of the order.
     */
    public int getTotalPoints() {
        int totalPoints = 0;
        for (Product p : this.products) {
            totalPoints += p.getPoints();
        }
        return totalPoints;
    }

    /**
     * Returns the date when the order was placed.
     *
     * @return the date of the Order
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the list of products in the order.
     *
     * @return the products list
     */

    public List<Product> getProducts() {
        return products;
    }

    /**
     * Returns the unique ID of the order.
     *
     * @return The unique order ID.
     */
    @Override
    public Integer getID() {
        return this.orderID;
    }


    public void setID(Integer id) {
        this.orderID = id;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order ID: ").append(this.orderID).append("\n");
        stringBuilder.append("Date: ").append(this.date).append("\n");
        stringBuilder.append("Products: \n");
        for (Product product : this.products) {
            stringBuilder.append(product.toString()).append("\n");
        }
        stringBuilder.append("Total: ").append(getTotal()).append("\n");

        return stringBuilder.toString();
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
}