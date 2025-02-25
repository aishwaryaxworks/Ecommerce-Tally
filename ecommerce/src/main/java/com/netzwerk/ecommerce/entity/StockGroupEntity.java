package com.netzwerk.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "stock_groups")
public class StockGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "parent")
    private String parent;
    @Column(name = "base_units")
    private String baseUnits;
    @Column(name = "additional_units")
    private String additionalUnits;
    @Column(name = "is_batch_wise_on")
    private String isBatchWiseOn;
    @Column(name = "is_perishable_on")
    private String isPerishableOn;
    @Column(name = "ignore_physical_difference")
    private String ignorePhysicalDifference;
    @Column(name = "ignore_negative_stock")
    private String ignoreNegativeStock;
}