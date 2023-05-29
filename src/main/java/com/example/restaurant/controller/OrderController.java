package com.example.restaurant.controller;

import com.example.restaurant.dto.OrderDTO;
import com.example.restaurant.entity.Order;
import com.example.restaurant.facade.OrderFacade;
import com.example.restaurant.service.OrderService;
import com.example.restaurant.validation.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrderById(Long.parseLong(orderId));
        OrderDTO orderDTO = orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                              BindingResult bindingResult,
                                              Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Order order = orderService.createOrder(orderDTO, principal);
        OrderDTO createdOrder = orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/{dishId}")
    public ResponseEntity<Object> addDishToOrder(@PathVariable String orderId,
                                                 @PathVariable String dishId) {
        Order order = orderService.addDishToOrder(Long.parseLong(orderId), Long.parseLong(dishId));
        OrderDTO orderDTO = orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/{dishId}")
    public ResponseEntity<Object> removeDishFromOrder(@PathVariable String orderId,
                                                 @PathVariable String dishId) {
        orderService.removeDishFromOrder(Long.parseLong(orderId), Long.parseLong(dishId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
