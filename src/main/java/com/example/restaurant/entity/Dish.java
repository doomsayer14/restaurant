package com.example.restaurant.entity;

import com.example.restaurant.entity.enums.DishCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String title;
    @Column
    private String ingredients;
    @Column
    private String description;
    @Column
    private DishCategory dishCategory;
    @Column
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;
}
