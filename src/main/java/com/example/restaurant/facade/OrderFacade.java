package com.example.restaurant.facade;

import com.example.restaurant.dto.OrderDTO;
import com.example.restaurant.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderFacade {
    public OrderDTO orderToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .dishList(order.getDishList())
                .price(order.getPrice())
                .user(order.getUser())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
