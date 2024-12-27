package org.confectionery.Domain;

import java.io.Serializable;

/**
 * The Admin class represents an administrator user in the system.
 * An admin can log in, view balance details, and perform other administrative tasks.
 */
public class Admin extends User implements Serializable {

    private String password;
    private String email;
    private Integer id;

    /**
     * @param password for the admin
     * @param email    for the admin
     * @param id       for the admin
     * @param name     for the admin
     * @param address  for the admin
     */
    public Admin(String password, String email, Integer id, String name, String address) {
        super(name, address);
        this.password = password;
        this.email = email;
        this.id = id;

    }

    /**
     * Gets the unique identifier of the admin.
     *
     * @return the admin id
     */

    @Override
    public Integer getID() {
        return this.id;
    }

    /**
     * @return the admin email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the admin password
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return a string containing the admins ID and email.
     */
    @Override
    public String toString() {
        return "Admin:" + "id" + " " + id + ", " + "user" + " " + name + ", "+ "email" + " " + email + ".";
    }
}