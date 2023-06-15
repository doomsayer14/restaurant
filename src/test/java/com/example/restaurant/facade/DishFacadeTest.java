package com.example.restaurant.facade;

import com.example.restaurant.dto.DishDTO;
import com.example.restaurant.entity.Dish;
import com.example.restaurant.entity.enums.DishCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DishFacadeTest {

    private DishFacade dishFacade;

    @BeforeEach
    void setUp() {
        dishFacade = new DishFacade();
    }

    @Test
    void dishToDishDTO_ConvertsDishToDishDTO() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setTitle("Pizza");
        dish.setIngredients("Cheese, Tomato Sauce, Pepperoni");
        dish.setDescription("Delicious pizza with pepperoni toppings");
        dish.setDishCategory(DishCategory.BREAKFAST);
        dish.setPrice(12.99);

        // Act
        DishDTO dishDTO = dishFacade.dishToDishDTO(dish);

        // Assert
        assertEquals(dish.getId(), dishDTO.getId());
        assertEquals(dish.getTitle(), dishDTO.getTitle());
        assertEquals(dish.getIngredients(), dishDTO.getIngredients());
        assertEquals(dish.getDescription(), dishDTO.getDescription());
        assertEquals(dish.getDishCategory(), dishDTO.getDishCategory());
        assertEquals(dish.getPrice(), dishDTO.getPrice());
    }
}
