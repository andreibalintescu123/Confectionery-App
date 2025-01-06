package org.confectionery.Domain;

import java.io.Serializable;

/**
 * The Admin class represents an administrator user in the system.
 * An admin can log in, view balance details, and perform other administrative tasks.
 */
public class Admin extends User implements Serializable {

    private String status;

    /**
     * @param password for the admin
     * @param email    for the admin
     * @param name     for the admin
     */
    public Admin(String name, String email, String password) {
        super(name,email, password);
        this.status = "Inactive";

    }

    public Admin(Integer id,String name, String email, String password) {
        super(id,name,email,password);
        this.status = "Inactive";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return a string containing the admins ID and email.
     */
    @Override
    public String toString() {
        return "Admin:" + "id" + " " + this.getID() + ", " + "username" + " " + name + ", "+ "email" + " " + email + ", " + "status" + " " + status + ".";
    }
}