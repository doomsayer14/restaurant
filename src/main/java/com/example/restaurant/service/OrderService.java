package com.example.restaurant.service;

import com.example.restaurant.dto.OrderDTO;
import com.example.restaurant.entity.Dish;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.User;
import com.example.restaurant.entity.enums.OrderStatus;
import com.example.restaurant.exception.OrderNotFoundException;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DishService dishService;

    public Order createOrder(OrderDTO orderDTO, Principal principal) {
        User user = getCurrentUser(principal);
        Order order = Order.builder()
                .dishList(orderDTO.getDishList())
                .price(orderDTO.getPrice())
                .user(user)
                .orderStatus(OrderStatus.NONE)
                .build();
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Dish cannot be found for id: " + id));
    }

    public Order addDishToOrder(Long orderId, Long dishId) {
        Order order = getOrderById(orderId);
        Dish dish = dishService.getDishById(dishId);
        order.add(dish);
        return order;
    }

    public void removeDishFromOrder(Long orderId, Long dishId) {
        Order order = getOrderById(orderId);
        Dish dish = dishService.getDishById(dishId);
        order.add(dish);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    public List<Order> getWaitingOrders() {
        return orderRepository.findAllByOrderStatusEquals(OrderStatus.NONE);
    }

    public Order changeStatus(Long orderId, Integer status) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(OrderStatus.values()[status]);
        return orderRepository.save(order);
    }
}
