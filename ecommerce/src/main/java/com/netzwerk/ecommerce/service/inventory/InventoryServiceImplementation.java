package com.netzwerk.ecommerce.service.inventory;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.entity.InventoryEntity;
import com.netzwerk.ecommerce.repository.InventoryRepository;
import com.netzwerk.ecommerce.service.tally.TallyInterface;
import com.netzwerk.ecommerce.util.InventoryUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class InventoryServiceImplementation implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryUtil inventoryUtil;
    private final TallyInterface tallyInterface;

    @Override
    public String sendInventoryToTally(InventoryDto inventoryDto, String username) {
            log.trace("Sending inventory data to Tally: {}", inventoryDto);
            if (inventoryDto.getSrcOfHsnDetails() == null || inventoryDto.getSrcOfHsnDetails().isEmpty()) {
                inventoryDto.setSrcOfHsnDetails("Specify Details Here");
            }
            InventoryEntity inventoryEntity = InventoryUtil.convertDtoToEntity(inventoryDto);
            inventoryEntity.setIsDeleted(false);
            inventoryEntity.setCreatedBy(username);
            inventoryEntity.setCreatedOn(LocalDateTime.now());
            inventoryRepository.save(inventoryEntity);
            log.info("Inventory saved to local database: {}", inventoryEntity);
            return "data saved successfully";
    }

    @Override
    public String checkForDuplicateInventory(String make, String model, String productCode) {
        log.info("Checking duplicate inventory for make: {}, model: {}, productCode: {}", make, model, productCode);
        InventoryEntity existingInventory = inventoryRepository.findByMakeModelAndProductCode(make, model, productCode);
        if (existingInventory != null) {
            return "An inventory item with the same make, model, and product code already exists.";
        }
        return "";
    }

    @Override
    public InventoryEntity findById(Integer id) {
        log.info("Finding inventory details using id : {}", id);
        return inventoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<InventoryEntity> findAll() {
        log.info("Fetching and displaying all active inventory details");
        return inventoryRepository.findByIsDeletedTrueOrderByIdDesc(); // Use a custom query method
    }

    @Override
    public void deleteById(Integer id) {
        try {
            InventoryEntity inventoryEntity = inventoryRepository.findById(id).orElse(null);
            if (inventoryEntity != null) {
                inventoryEntity.setIsDeleted(true);
                inventoryEntity.setUpdatedOn(LocalDateTime.now());
                inventoryRepository.save(inventoryEntity);
                log.info("Soft deleted inventory with ID: {}", id);
            } else {
                log.warn("Inventory not found with ID: {}", id);
            }
        } catch (Exception e) {
            log.error("An error occurred while attempting to soft delete inventory with ID {}: {}", id, e.getMessage());
        }
    }

    public InventoryDto convertEntityToDto(InventoryEntity entity) {
        InventoryDto dto = new InventoryDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public Page<InventoryDto> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryEntity> inventoryPage = inventoryRepository.findByIsDeletedFalseOrderByIdDesc(pageable);
        List<InventoryDto> inventoryDtos = inventoryPage.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(inventoryDtos, pageable, inventoryPage.getTotalElements());
    }

    @Override
    public Page<InventoryEntity> getPaginatedInventory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return inventoryRepository.findAll(pageable);
    }

    @Override
    public void updateInventory(InventoryDto inventoryDto) {
        InventoryEntity existingInventory = inventoryRepository.findById(inventoryDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        InventoryUtil.copyDtoToEntity(inventoryDto, existingInventory);
        existingInventory.setUpdatedOn(LocalDateTime.now());
        inventoryRepository.save(existingInventory);
        log.info("Inventory updated: {}", existingInventory);
    }

    @Override
    public List<String> suggestionByMake(String make) {
        return inventoryRepository.suggestionByMake(make);
    }

    @Override
    public Page<InventoryDto> searchByMake(String make, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<InventoryEntity> entities = inventoryRepository.findByMakeContainingIgnoreCase(make, pageable);
        List<InventoryDto> inventoryDtos = entities.stream()
                .map(inventoryUtil::convertEntityToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(inventoryDtos, pageable, entities.getTotalElements());
    }

    public List<InventoryDto> fetchInventoryFromTally(String companyName) {
        log.info("Fetching inventory from Tally for company: {}", companyName);
        List<InventoryDto> inventoryList = tallyInterface.fetchInventory(companyName);
        log.info("Response from Tally API: {}", inventoryList);
        return inventoryList;
    }

    @Override
    public List<InventoryDto> fetchInventoryByGroup(String companyName, String groupName) {
        log.info("Fetching inventory from Tally for company: {} and group: {}", companyName, groupName);
        List<InventoryDto> inventoryList = tallyInterface.fetchInventoryByGroup(companyName, groupName);
        log.info("Filtered response from Tally API for Group {}: {}", groupName, inventoryList);
        return inventoryList;
    }


    @Override
    public InventoryDto findByMakeModuleAndProductCode(String make, String model, String productCode) {
        InventoryEntity entity = inventoryRepository.findByMakeModuleAndProductCode(make, model, productCode);
        if (entity == null) {
            return null;
        }
        return convertEntityToDto(entity);
    }

    @Override
    public void updateLocalDatabase(InventoryDto inventoryDto, String username) {
        InventoryEntity existingInventory = inventoryRepository.findById(inventoryDto.getId()).orElse(null);
        if (existingInventory == null) {
            log.warn("Inventory not found with ID: {}", inventoryDto.getId());
            return;
        }
        String existingGuid = existingInventory.getGuidOfInventory();
        if (existingGuid != null && !existingGuid.equals("NA") && !existingGuid.trim().isEmpty()) {
            inventoryDto.setGuidOfInventory(existingGuid);
        }
        inventoryDto.setItemName(inventoryDto.getMake() + "-" + inventoryDto.getModel() + "-" + inventoryDto.getProductCode());
        InventoryUtil.copyDtoToEntity(inventoryDto, existingInventory);
        existingInventory.setUpdatedBy(username);
        existingInventory.setUpdatedOn(LocalDateTime.now());
        inventoryRepository.save(existingInventory);
        log.info("Inventory updated in local database by {}", username);
    }

    @Override
    public InventoryDto getInventoryDtoById(Integer id) {
        InventoryEntity entity = inventoryRepository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        InventoryDto dto = convertEntityToDto(entity);
        if (dto.getItemName() == null || dto.getItemName().trim().isEmpty()) {
            dto.setItemName(dto.getMake() + "-" + dto.getModel() + "-" + dto.getProductCode());
        }
        return dto;
    }

}
