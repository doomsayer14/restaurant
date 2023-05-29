package com.example.restaurant.dto;

import com.example.restaurant.entity.Dish;
import com.example.restaurant.entity.User;
import com.example.restaurant.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private List<Dish> dishList;
    private Money price;
    private User user;
    private OrderStatus orderStatus;
}
