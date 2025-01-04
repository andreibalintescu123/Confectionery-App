package org.confectionery.Controller;

import org.confectionery.Domain.Admin;
import org.confectionery.Domain.Client;
import org.confectionery.Domain.User;
import org.confectionery.Service.ConfectioneryService;

import java.util.List;

public class ConfectioneryController {
    private final ConfectioneryService confectioneryService;

    public ConfectioneryController(ConfectioneryService confectioneryService) {
        this.confectioneryService = confectioneryService;
    }

    public Admin createAdmin(String name,String email,String password) {
        return confectioneryService.createAdmin(name, email, password);
    }

    public Client createClient(String name, String email, String password, String address) {
        return confectioneryService.createClient(name, email, password, address);
    }

    public User loginUser(String email, String password) {
        return confectioneryService.loginUser(email,password);
    }

    public List<User> getUsers() {
        return confectioneryService.getUsers();
    }

    public void updateStatusOn(Integer id) {
        confectioneryService.statusOn(id);
    }

    public void updateStatusOff(Integer id) {
        confectioneryService.statusOff(id);
    }
}
