package com.example.restaurant.repository;

import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderStatusEquals(OrderStatus orderStatus);
}
