package com.example.restaurant.service;

import com.example.restaurant.dto.DishDTO;
import com.example.restaurant.entity.Dish;
import com.example.restaurant.entity.enums.DishCategory;
import com.example.restaurant.exception.DishNotFoundException;
import com.example.restaurant.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDishById() {
        Long dishId = 1L;
        Dish dish = new Dish();

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        Dish retrievedDish = dishService.getDishById(dishId);

        assertNotNull(retrievedDish);
        assertEquals(dish, retrievedDish);

        verify(dishRepository, times(1)).findById(dishId);
    }

    @Test
    void testGetDishById_DishNotFoundException() {
        Long dishId = 1L;

        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class, () -> dishService.getDishById(dishId));

        verify(dishRepository, times(1)).findById(dishId);
    }

    @Test
    void testCreateDish() {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setTitle("Dish Title");
        dishDTO.setIngredients("Ingredient 1, Ingredient 2");
        dishDTO.setDescription("Dish Description");
        dishDTO.setDishCategory(DishCategory.MEAT);
        dishDTO.setPrice(10.99);

        Dish dish = Dish.builder()
                .title(dishDTO.getTitle())
                .ingredients(dishDTO.getIngredients())
                .description(dishDTO.getDescription())
                .dishCategory(dishDTO.getDishCategory())
                .price(dishDTO.getPrice())
                .build();

        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish createdDish = dishService.createDish(dishDTO);

        assertNotNull(createdDish);
        assertEquals(dishDTO.getTitle(), createdDish.getTitle());
        assertEquals(dishDTO.getIngredients(), createdDish.getIngredients());
        assertEquals(dishDTO.getDescription(), createdDish.getDescription());
        assertEquals(dishDTO.getDishCategory(), createdDish.getDishCategory());
        assertEquals(dishDTO.getPrice(), createdDish.getPrice());

        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    void testGetAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish());
        dishes.add(new Dish());

        when(dishRepository.findAll()).thenReturn(dishes);

        List<Dish> retrievedDishes = dishService.getAllDishes();

        assertNotNull(retrievedDishes);
        assertEquals(2, retrievedDishes.size());

        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void testDeleteDish() {
        Long dishId = 1L;

        dishService.deleteDish(dishId);

        verify(dishRepository, times(1)).deleteById(dishId);
    }
}
