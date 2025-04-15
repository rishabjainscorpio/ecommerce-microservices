package com.myorg.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name="OrderLineItem")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String skuCode;
    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;
}
