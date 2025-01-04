package org.confectionery.Service;

import org.confectionery.Domain.*;
import org.confectionery.Repository.Repository;

import java.util.List;

public class ConfectioneryService {
    private final UserService userService;

    public ConfectioneryService(UserService userService) {
        this.userService = userService;
    }
    public Admin createAdmin(String name, String email, String password) {
        return userService.registerAdmin(name,email,password);
    }

    public Client createClient(String name, String email, String password, String address) {
        return userService.registerClient(name,email,password,address);
    }

    public User loginUser(String email, String password) {
        return userService.login(email,password);
    }

    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    public void statusOn(Integer id) {
        userService.setStatusOn(id);
    }

    public void statusOff(Integer id) {
        userService.setStatusOff(id);
    }
}
