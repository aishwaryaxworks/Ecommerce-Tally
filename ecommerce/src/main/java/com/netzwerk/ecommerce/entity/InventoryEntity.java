package com.netzwerk.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Component
@Entity
@Data
@Table(name = "inventory")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "src_of_hsn_details")
    private String srcOfHsnDetails = "Specify Details Here";
    @Column(name = "src_of_gst_details")
    private String srcOfGstDetails = "Specify Details Here";
    @Column(name = "gst_calc_slab_on_mrp")
    private String gstCalcSlabOnMrp;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "sub_group_name")
    private String subGroupName;
    @Column(name = "make")
    private String make;
    @Column(name = "model")
    private String model;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "hsn_code")
    private String hsnCode;
    @Column(name = "cgst_rate")
    private Double cgstRate;
    @Column(name = "sgst_rate")
    private Double sgstRate;
    @Column(name = "igst_rate")
    private Double igstRate;
    @Column(name = "opening_balance")
    private Integer openingBalance;
    @Column(name = "applicable_from_hsn")
    private String applicableFromHsn;
    @Column(name = "applicable_from_gst")
    private String applicableFromGst;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
    @Column(name = "rate")
    private Integer rate = 0;
    @Column(name = "base_unit")
    private String baseUnit = "Nos";
    @Column(name = "opening_value")
    private Integer openingValue = 0;
    @Column(name = "taxability")
    private String taxability;
    @Column(name = "is_reverse_charge_applicable")
    private String isReverseChargeApplicable = "No";
    @Column(name = "is_non_gst_goods")
    private String isNonGstGoods;
    @Column(name = "cess_rate")
    private Integer cessRate = 0;
    @Column(name = "state_cess_rate")
    private Integer stateCessRate = 0;
    @Column(name = "guidOfInventory")
    private String guidOfInventory = "NA";
    @Column(name = "presentInTally")
    private Boolean presentInTally = false;
}

