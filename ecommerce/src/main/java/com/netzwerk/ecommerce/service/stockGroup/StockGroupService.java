package com.netzwerk.ecommerce.service.stockGroup;

import com.netzwerk.ecommerce.dto.StockGroupDto;

import java.util.List;

public interface StockGroupService {
    void createStockGroup(StockGroupDto stockGroupDto);
    List<String> getAllStockGroupNames();
    boolean isStockGroupNameExists(String name);
}
