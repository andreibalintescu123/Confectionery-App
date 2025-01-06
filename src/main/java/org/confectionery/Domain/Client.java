package org.confectionery.Domain;

import java.io.Serializable;

/**
 * Represents a client who can place orders and view invoices
 */
public class Client extends User implements Serializable {

    private String address;

    /**
     * @param name     represents the client name
     * @param address  represents the client address
     * @param password represents the client password
     * @param email    represents the client email
     */
    public Client(String name, String email, String password, String address) {
        super(name, email, password);
        this.address = address;
    }

    public Client(Integer id, String name, String email, String password, String address) {
        super(id, name, email, password);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return a string with the client id ,name and address
     */
    @Override
    public String toString() {
        return "Client: " + "id " + this.getID() + "," + "name " + name + "," + "address " + address;
    }


}