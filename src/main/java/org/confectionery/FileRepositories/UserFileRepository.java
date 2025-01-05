package org.confectionery.FileRepositories;

import org.confectionery.Domain.Admin;
import org.confectionery.Domain.Client;
import org.confectionery.Domain.User;
import org.confectionery.Repository.FileRepository;

public class UserFileRepository extends FileRepository<User> {

    public UserFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected String serialize(User obj) {
        if (obj instanceof Admin admin) {
            return "Admin," + admin.getID() + "," + admin.getName() + "," + admin.getEmail() + "," + admin.getPassword() + "," + admin.getStatus();
        } else if (obj instanceof Client client) {
            return "Client," + client.getID() + "," + client.getName() + "," + client.getEmail() + "," + client.getPassword() + "," + client.getAddress();
        }
        throw new IllegalArgumentException("Unknown User type");
    }

    @Override
    protected User deserialize(String data) {
        try {
            String[] parts = data.split(",");
            String type = parts[0];
            int id = Integer.parseInt(parts[1]);

            if ("Admin".equals(type)) {
                String name = parts[2];
                String email = parts[3];
                String password = parts[4];
                String status = parts[5];
                Admin admin = new Admin(name, email, password);
                admin.setID(id);
                admin.setStatus(status);
                return admin;
            } else if ("Client".equals(type)) {
                String name = parts[2];
                String email = parts[3];
                String password = parts[4];
                String address = parts[5];
                Client client = new Client(name, email, password, address);
                client.setID(id);
                return client;
            }

            throw new IllegalArgumentException("Unknown User type: " + type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing User: " + data, e);
        }
    }
}

