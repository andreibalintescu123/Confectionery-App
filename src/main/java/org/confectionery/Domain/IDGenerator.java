package org.confectionery.Domain;


/**
 * A singleton class to generate unique IDs for all entities of the domain.
 */
public class IDGenerator {
    private static IDGenerator instance;
    private int currentId;

    // Private constructor to prevent instantiation
    private IDGenerator() {
        this.currentId = 1;
    }

    // Method to get the singleton instance
    public static synchronized IDGenerator getInstance() {
        if (instance == null) {
            instance = new IDGenerator();
        }
        return instance;
    }

    // Method to generate a unique ID
    public synchronized int generateID() {
        return currentId++;
    }

    // Method to reset the ID generator, for testing purposes
    public synchronized void reset() {
        this.currentId = 1;
    }

}
