package org.confectionery.Service;

import org.confectionery.Domain.Order;
import org.confectionery.Repository.Repository;

import java.time.LocalDate;
import java.util.List;

public class OrderService {
    private final Repository<Order> orderRepository;

    public OrderService(Repository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(Order order) {
        orderRepository.create(order);
    }

    public Order findOrderById(int id) {
        return orderRepository.get(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAll();
    }

    public void updateOrder(Order order) {
        orderRepository.update(order);
    }

    public void deleteOrder(int id) {
        orderRepository.delete(id);
    }

    public List<Order> findOrdersByClientId(int clientId) {
        return getAllOrders().stream()
                .filter(order -> order.getClientID() == clientId)
                .toList();
    }

    public List<Order> findOrdersByDate(LocalDate date) {
        return getAllOrders().stream()
                .filter(order -> order.getDate().equals(date))
                .toList();
    }
}
