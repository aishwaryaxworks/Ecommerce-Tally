package com.netzwerk.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "order_info")
@Component
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "customer_payable_amount")
    private Double customerPayableAmount;

    @Column(name = "purchase_order_number")
    private String purchaseOrderNumber;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "model")
    private String model;

    @Column(name = "make")
    private String make;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "opening_balance")
    private Integer openingBalance;

    @Column(name = "opening_value")
    private Integer openingValue;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "selling_price")
    private Double sellingPrice;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "narration")
    private String narration;

    @Column(name = "voucherTypeName")
    private String voucherTypeName;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "order_due_date")
    private LocalDate orderDueDate;

    @Column(name = "isInvoice")
    private String isInvoice;

    @Column(name="o_guid")
    private String orderGuid ="NA";

    @Column(name="order_status")
    private Boolean orderStatus = false;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerDetailsEntity customerDetailsEntity;

}
