package com.example.restaurant.service;

import com.example.restaurant.dto.DishDTO;
import com.example.restaurant.entity.Dish;
import com.example.restaurant.exception.DishNotFoundException;
import com.example.restaurant.repository.DishRepository;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish cannot be found for id: " + id));
    }

    public Dish createDish(DishDTO dishDTO) {
        Dish dish = Dish.builder()
                .title(dishDTO.getTitle())
                .ingredients(dishDTO.getIngredients())
                .description(dishDTO.getDescription())
                .dishCategory(dishDTO.getDishCategory())
                .price(dishDTO.getPrice())
                .build();

        log.info("Saving dish: {}", dish);
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
