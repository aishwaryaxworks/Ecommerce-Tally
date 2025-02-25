package com.netzwerk.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Table(name = "payment_details")
@Data
@Builder

public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", updatable = false, nullable = false)
    private int paymentId;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;
    private String paymentMode;
    private double paidAmount;
    private double remainingBalance;
    private LocalDate nextPaymentDate;
    private String createdBy;
    private LocalDate createdOn;

}
