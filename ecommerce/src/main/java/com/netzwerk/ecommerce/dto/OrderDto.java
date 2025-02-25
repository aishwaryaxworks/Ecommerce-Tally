package com.netzwerk.ecommerce.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Component

public class OrderDto {

    private Integer id;
    private Double customerPayableAmount;
    private String purchaseOrderNumber;
    private String groupName;
    private String model;
    private String make;
    private String productCode;
    private Integer openingValue = 0;
    private Double sellingPrice = 0.0;
    private Integer openingBalance = 0;
    private Integer quantity = 0;
    private Double totalPrice = 0.0;
    private String createdBy ;
    private LocalDateTime createdOn;
    private String updatedBy ;
    private LocalDateTime updatedOn;
    private Boolean isActive = true;
    private String customerName;
    private String narration;
    private String voucherTypeName;
    private String itemName;
    private LocalDate orderDueDate;
    private String isInvoice ;
    private String orderGuid ;
    private Boolean orderStatus = false;
    private Integer customerId;

}
