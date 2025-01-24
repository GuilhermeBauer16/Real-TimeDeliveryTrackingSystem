package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
