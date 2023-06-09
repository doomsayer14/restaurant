package com.example.restaurant.dto;

import com.example.restaurant.entity.enums.DishCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishDTO {
    private Long id;
    private String title;
    private String ingredients;
    private String description;
    private DishCategory dishCategory;
    private Double price;
}
