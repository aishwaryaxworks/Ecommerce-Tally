package com.netzwerk.ecommerce.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class CustomerDetailsDto {
    private Integer customerId;
    private String guid;
    private String customerName;
    private String customerEmailId;
    private String customerType;
    private Long contactNumber;
    private String gstNumber;
    private String country;
    private String state;
    private String city;
    private Integer pinCode;
    private String address1;
    private String address2;
    private String address3;
    private String billingAddress;
    private String shippingAddress;
    private String paymentMode;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
    private Boolean isActive=true;
    private Double customerCredit;
    private Double customerPayableAmount;

}
