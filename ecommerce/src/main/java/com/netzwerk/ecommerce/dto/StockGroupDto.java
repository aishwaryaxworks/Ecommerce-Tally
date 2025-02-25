package com.netzwerk.ecommerce.dto;

import lombok.Data;

@Data
public class StockGroupDto {
    private String name;
    private String parent;
    private String baseUnits;
    private String additionalUnits;
    private String isBatchWiseOn;
    private String isPerishableOn;
    private String ignorePhysicalDifference;
    private String ignoreNegativeStock;
}

