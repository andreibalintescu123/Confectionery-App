package org.confectionery.Service;

import org.confectionery.Domain.Admin;
import org.confectionery.Domain.Client;
import org.confectionery.Domain.User;
import org.confectionery.Exception.InvalidCredentialsException;
import org.confectionery.Repository.Repository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final Repository<User> userRepository;

    public UserService(Repository<User> userRepository) {
        this.userRepository = userRepository;
    }
    public Client registerClient(String name, String email,String password, String address) {
        try{
            if (isEmailRegistered(email)) {
                throw new IllegalArgumentException("A user with this email already exists.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return null;
        }

        Client client = new Client(name, email, password, address);
        userRepository.create(client);
        return client;
    }

    public Admin registerAdmin(String name, String email, String password) {
        try{
            if (isEmailRegistered(email)) {
                throw new IllegalArgumentException("A user with this email already exists.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return null;
        }

        Admin admin = new Admin(name, email, password);
        userRepository.create(admin);
        return admin;
    }

    public User login(String email, String password) {
        try{
            Optional<User> loggedUser = userRepository.getAll().stream()
                    .filter(user -> user instanceof Admin || user instanceof Client)
                    .filter(user -> user instanceof Admin admin && admin.getEmail().equals(email) && admin.getPassword().equals(password)
                            || user instanceof Client client && client.getEmail().equals(email) && client.getPassword().equals(password))
                    .findFirst();
            if(loggedUser.isPresent()){
                return loggedUser.get();
            }
            else throw new InvalidCredentialsException("Incorrect credentials provided or not found.");
        }catch (InvalidCredentialsException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public void deleteUser(Integer id) {
        userRepository.delete(id);
    }

    public void resetClientPassword(Client client, String newPassword) {
        client.setPassword(newPassword);
        userRepository.update(client);
    }
    private boolean isEmailRegistered(String email) {
        return userRepository.getAll()
                .stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public void setStatusOn(Integer id) {
        Admin admin = (Admin) userRepository.get(id);
        admin.setStatus("Active");
        userRepository.update(admin);
    }

    public void setStatusOff(Integer id) {
        Admin admin = (Admin) userRepository.get(id);
        admin.setStatus("Inactive");
        userRepository.update(admin);
    }
}
