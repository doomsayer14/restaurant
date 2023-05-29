package com.example.restaurant.facade;

import com.example.restaurant.dto.DishDTO;
import com.example.restaurant.entity.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishFacade {
    public DishDTO dishToDishDTO(Dish dish) {
        return DishDTO.builder()
                .id(dish.getId())
                .title(dish.getTitle())
                .ingredients(dish.getIngredients())
                .description(dish.getDescription())
                .dishCategory(dish.getDishCategory())
                .price(dish.getPrice())
                .build();
    }
}
