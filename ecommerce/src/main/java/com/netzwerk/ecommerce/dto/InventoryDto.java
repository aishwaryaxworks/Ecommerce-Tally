package com.netzwerk.ecommerce.dto;


import lombok.*;
import org.springframework.stereotype.Component;
import jakarta.validation.constraints.*;


@Data
@Component
@Getter
@Setter
@ToString
public class InventoryDto {

    private Integer id;
    @NotBlank(message = "Group name cannot be blank")
    private String groupName;
    private String subGroupName;
    @NotBlank(message = "Make cannot be blank")
    private String make;
    @NotBlank(message = "Model cannot be blank")
    private String model;
    @NotBlank(message = "Product code cannot be blank")
    private String productCode;
    @NotBlank(message = "HSN code cannot be blank")
    private String hsnCode;
    @NotNull(message = "CGST rate is required")
    private Double cgstRate;
    @NotNull(message = "SGST/UTGST rate is required")
    private Double sgstRate;
    @NotNull(message = "IGST rate is required")
    private Double igstRate;
    @NotNull(message = "Opening balance is required")
    private Integer openingBalance;
    @NotBlank(message = "Applicable from HSN is required")
    private String applicableFromHsn;
    @NotBlank(message = "Applicable from GST is required")
    private String applicableFromGst;
    @NotBlank(message = "Item name is required")
    private String itemName;
    @NotNull(message = "Rate is required")
    private Integer rate=0;
    private String baseUnit = "Nos";
    private Integer openingValue = 0;
    private String srcOfHsnDetails = "Specify Details Here";
    private String srcOfGstDetails = "Specify Details Here";
    @NotBlank(message = "Taxability is required")
    private String taxability="Taxable";
    private String gstCalcSlabOnMrp="No";
    @NotBlank(message = "Reverse charge applicability cannot be blank")
    private String isReverseChargeApplicable="No";
    private String isNonGstGoods="No";
    @DecimalMin(value = "0", message = "Cess rate cannot be negative")
    private Integer cessRate=0;
    private Integer stateCessRate = 0;
    private String guidOfInventory ="NA";
    private String createdBy;
    private String updatedBy;
    private String createdOn;
    private String updatedOn;

}