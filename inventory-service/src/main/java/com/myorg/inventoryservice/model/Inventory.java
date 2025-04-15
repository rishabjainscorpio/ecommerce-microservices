package com.myorg.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name="inventory")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String skuCode;

    private int quantity;
}
