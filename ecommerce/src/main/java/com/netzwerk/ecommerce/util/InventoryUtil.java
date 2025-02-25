package com.netzwerk.ecommerce.util;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.entity.InventoryEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class InventoryUtil {
    private InventoryUtil(){
    }

    public InventoryDto convertEntityToDto(InventoryEntity entity) {
        if (entity == null) {
            return null;
        }
        InventoryDto dto = new InventoryDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setGroupName(entity.getGroupName());
        dto.setMake(defaultValue(entity.getMake()));
        dto.setModel(defaultValue(entity.getModel()));
        dto.setProductCode(defaultValue(entity.getProductCode()));
        dto.setItemName(defaultValue(entity.getItemName()));
        dto.setHsnCode(defaultValue(entity.getHsnCode()));
        dto.setBaseUnit(defaultValue(entity.getBaseUnit()));
        dto.setSubGroupName(defaultValue(entity.getSubGroupName()));
        return dto;
    }

    public static InventoryEntity convertDtoToEntity(InventoryDto dto) {
        if (dto == null) {
            return null;
        }
        InventoryEntity entity = new InventoryEntity();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getBaseUnit() == null) {
            entity.setBaseUnit("Nos");
        }
        if (entity.getSrcOfHsnDetails() == null || entity.getSrcOfHsnDetails().isEmpty()) {
            entity.setSrcOfHsnDetails("Specify Details Here");
        }
        if (entity.getOpeningValue() == null) {
            entity.setOpeningValue(0);
        }
        entity.setIsDeleted(false);
        entity.setCreatedOn(java.time.LocalDateTime.now());
        entity.setUpdatedOn(java.time.LocalDateTime.now());
        entity.setMake(defaultValue(dto.getMake()));
        entity.setModel(defaultValue(dto.getModel()));
        entity.setProductCode(defaultValue(dto.getProductCode()));
        entity.setItemName(defaultValue(dto.getItemName()));
        entity.setHsnCode(defaultValue(dto.getHsnCode()));
        entity.setSubGroupName(defaultValue(dto.getSubGroupName()));
        if (entity.getGuidOfInventory() != null && !entity.getGuidOfInventory().equals("NA") && !entity.getGuidOfInventory().trim().isEmpty()) {
            dto.setGuidOfInventory(entity.getGuidOfInventory());
        } else {
            entity.setGuidOfInventory(dto.getGuidOfInventory());
        }
        return entity;
    }

    public static void copyDtoToEntity(InventoryDto dto, InventoryEntity entity) {
        entity.setGroupName(dto.getGroupName());
        entity.setSubGroupName(dto.getSubGroupName());
        entity.setMake(dto.getMake());
        entity.setModel(dto.getModel());
        entity.setProductCode(dto.getProductCode());
        entity.setHsnCode(dto.getHsnCode());
        entity.setCgstRate(dto.getCgstRate());
        entity.setSgstRate(dto.getSgstRate());
        entity.setIgstRate(dto.getIgstRate());
        entity.setOpeningBalance(dto.getOpeningBalance());
        entity.setApplicableFromHsn(dto.getApplicableFromHsn());
        entity.setApplicableFromGst(dto.getApplicableFromGst());
        entity.setItemName(dto.getItemName());
        entity.setBaseUnit(dto.getBaseUnit() != null ? dto.getBaseUnit() : "Nos");
        entity.setSrcOfHsnDetails(dto.getSrcOfHsnDetails() != null ? dto.getSrcOfHsnDetails() : "Specify Details Here");
        entity.setOpeningValue(dto.getOpeningValue() != null ? dto.getOpeningValue() : 0);
        entity.setTaxability(dto.getTaxability());
        entity.setGstCalcSlabOnMrp(dto.getGstCalcSlabOnMrp());
        entity.setIsReverseChargeApplicable("No");
        entity.setIsNonGstGoods(dto.getIsNonGstGoods());
        if (entity.getGuidOfInventory() != null && !entity.getGuidOfInventory().equals("NA") && !entity.getGuidOfInventory().trim().isEmpty()) {
            dto.setGuidOfInventory(entity.getGuidOfInventory());
        } else {
            entity.setGuidOfInventory(dto.getGuidOfInventory());
        }
    }

    private static String defaultValue(String value) {
        return (value == null || value.trim().isEmpty()) ? "NA" : value;
    }

}
