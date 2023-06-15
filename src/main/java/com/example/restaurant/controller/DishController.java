package com.example.restaurant.controller;

import com.example.restaurant.dto.DishDTO;
import com.example.restaurant.entity.Dish;
import com.example.restaurant.facade.DishFacade;
import com.example.restaurant.service.DishService;
import com.example.restaurant.validation.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFacade dishFacade;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/{dishId}")
    public ResponseEntity<DishDTO> getDish(@PathVariable String dishId) {
        Dish dish = dishService.getDishById(Long.parseLong(dishId));
        DishDTO dishDTO = dishFacade.dishToDishDTO(dish);
        return new ResponseEntity<>(dishDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishDTOList = dishService.getAllDishes()
                .stream()
                .map(dishFacade::dishToDishDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dishDTOList, HttpStatus.OK);
    }

    @PostMapping("/manager/create")
    public ResponseEntity<Object> createDish(@Valid @RequestBody DishDTO dishDTO,
                                              BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Dish dish = dishService.createDish(dishDTO);
        DishDTO createdDish = dishFacade.dishToDishDTO(dish);
        return new ResponseEntity<>(createdDish, HttpStatus.OK);
    }

    @DeleteMapping("/manager/{dishId}")
    public ResponseEntity<HttpStatus> deleteDish(@PathVariable("dishId") String dishId) {
        dishService.deleteDish(Long.parseLong(dishId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
