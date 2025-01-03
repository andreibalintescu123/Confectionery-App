package org.confectionery.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client who can place orders and view invoices
 */
public class Client extends User implements Serializable {

    private Integer ClientID;
    private final List<Order> orders = new ArrayList<>();

    /**
     * @param name    represents the client name
     * @param address represents the client adress
     * @param Id      represents the client id
     */
    public Client(String name, String address, Integer Id) {
        super(name, address);
        this.ClientID = Id;
    }

    /**
     * @return the client id
     **/

    @Override
    public Integer getID() {
        return this.ClientID;
    }

    /**
     * @return the orders
     */
    public List<Order> getOrders() {
        return this.orders;
    }

    /**
     * Places a new order for the client.
     *
     * @param order the object that represents the new order.
     */
    public void placeOrder(Order order) {
        this.orders.add(order);
    }

    /**
     * Prints the invoice for all the orders placed by the client.
     * It shows the products ordered, their prices, and points, as well as the total for each order.
     * If there are multiple orders, it also shows the grand total and total points.
     */
    public void getInvoice() {
        if (this.orders.isEmpty()) {
            System.out.println("You have no orders yet." + "\n");
            return;
        }
        for (Order order : this.orders) {
            System.out.println("Order Id:" + order.getID());
            for (Product product : order.getProducts()) {
                System.out.println(product.getName() + "................................." + product.getPrice() + "lei" + "---------" + product.getPoints() + " points");
            }
            System.out.print("Your total for this order is: " + order.getTotal() + "lei" + "\n");
            System.out.println("Your total points for this order " + order.getTotalPoints() + "\n");
        }
        if (orders.size() > 1) {
            System.out.println("Your grand total is:" + this.grandTotal() + "lei");
            System.out.println("You have " + this.grandTotalPoints() + " points");
        }
    }

    /**
     * @return a string with the client id ,name and adress
     */
    @Override
    public String toString() {
        return "Client: " + "id " + ClientID + "," + "name " + name + "," + "address " + address;
    }

    /**
     * @return the username
     */

    public Object getUsername() {
        return this.name;
    }

    /**
     * Calculates the grand total of all the orders placed by the client.
     *
     * @return the total of all orders
     */
    public float grandTotal() {
        float total = 0;
        for (Order order : this.orders) {
            total += order.getTotal();
        }
        return total;
    }

    /**
     * Calculates the grand total of points of all the orders placed by the client.
     *
     * @return the total points which a client has accumulated
     */
    public int grandTotalPoints() {
        int total = 0;
        for (Order order : this.orders) {
            total += order.getTotalPoints();
        }
        return total;
    }

    /**
     * Deletes an order from the client's order history by its ID.
     *
     * @param id The ID of the order to be deleted.
     */

    public void deleteById(int id) {
        for (Order order : this.orders) {
            if (order.getID() == id) {
                this.orders.remove(order);
                break;
            }
        }
    }
}