package org.confectionery.Domain;
import java.io.*;

/**
 * main class User ,which implements the HasId interface
 * there are 2 types of users ->client and admin
 */
public abstract class User implements HasID, Serializable {


    protected Integer userID;
    protected String name;
    protected String email;
    protected String password;

    /**
     *
     * @param name represents the username
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userID = IDGenerator.getInstance().generateID();
    }


    /**
     * Gets the name of the user.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of the User.
     * @return the id of the user.
     */
    @Override
    public Integer getID(){
        return userID;
    }

    public void setID(Integer userID) {
        this.userID = userID;
    }
    public abstract String toString();

}
