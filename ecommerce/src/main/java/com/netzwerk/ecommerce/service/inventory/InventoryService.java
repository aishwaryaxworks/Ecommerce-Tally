package com.netzwerk.ecommerce.service.inventory;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.entity.InventoryEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    InventoryEntity findById(Integer id);

    List<InventoryEntity> findAll();

    Page<InventoryDto> findPaginated(int pageNo, int pageSize);

    Page<InventoryEntity> getPaginatedInventory(int page, int size);

    void deleteById(Integer id);

    void updateInventory(InventoryDto inventoryDto);

    String checkForDuplicateInventory(String make, String model, String productCode);

    List<InventoryDto> fetchInventoryByGroup(String companyName, String groupName);

    List<String> suggestionByMake(String make);

    Page<InventoryDto> searchByMake(String make,int pageNo, int pageSize);

    InventoryDto findByMakeModuleAndProductCode(String make, String model, String productCode);

    String sendInventoryToTally(InventoryDto inventoryDto, String username);

    void updateLocalDatabase(InventoryDto inventoryDto, String username);

    InventoryDto getInventoryDtoById(Integer id);
}
