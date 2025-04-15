package com.myorg.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.util.List;

@Entity
@Table(name="Orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String orderNumber;

    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL)
    private List<OrderLineItems> orderLineItems;
}
