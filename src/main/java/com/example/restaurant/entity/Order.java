package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.restaurant.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "order")
    private List<Dish> dishList = new ArrayList<>();
    @Column
    private Money price = Money.zero(Monetary.getCurrency("UAH"));
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @Column
    private OrderStatus orderStatus;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    public void add(Dish dish) {
        dishList.add(dish);
    }

    public void remove(Dish dish) {
        dishList.remove(dish);
    }
}
