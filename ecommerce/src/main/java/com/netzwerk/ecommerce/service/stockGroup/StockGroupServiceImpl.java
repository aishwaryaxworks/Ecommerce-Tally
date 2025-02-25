package com.netzwerk.ecommerce.service.stockGroup;

import com.netzwerk.ecommerce.dto.StockGroupDto;
import com.netzwerk.ecommerce.entity.StockGroupEntity;
import com.netzwerk.ecommerce.repository.StockGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class StockGroupServiceImpl implements StockGroupService {

    private final StockGroupRepository stockGroupRepository;

    @Override
    public void createStockGroup(StockGroupDto stockGroupDto) {
        StockGroupEntity entity = new StockGroupEntity();
        entity.setName(stockGroupDto.getName());
        entity.setParent(stockGroupDto.getParent() == null ? "None" : stockGroupDto.getParent());
        entity.setBaseUnits("Nos");
        entity.setIsBatchWiseOn("No");
        entity.setIsPerishableOn("No");
        entity.setIgnorePhysicalDifference("No");
        entity.setIgnoreNegativeStock("No");
        stockGroupRepository.save(entity);
    }

    @Override
    public List<String> getAllStockGroupNames() {
        return stockGroupRepository.findAllGroupNames();
    }

    @Override
    public boolean isStockGroupNameExists(String name) {
        return stockGroupRepository.existsByName(name);
    }
}