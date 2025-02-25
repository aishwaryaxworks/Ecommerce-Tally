package com.netzwerk.ecommerce.dto.payment;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDto {


    private int paymentId;
    private int orderId;
    private String paymentMode;
    private double paidAmount;
    private double remainingBalance;
    private LocalDate nextPaymentDate;
    private String createdBy;
    private LocalDate createdOn;

}
