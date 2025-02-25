package com.netzwerk.ecommerce.resources;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.entity.InventoryEntity;
import com.netzwerk.ecommerce.service.inventory.InventoryService;
import com.netzwerk.ecommerce.service.inventory.InventoryServiceImplementation;
import com.netzwerk.ecommerce.service.stockGroup.StockGroupService;
import com.netzwerk.ecommerce.util.InventoryUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
@AllArgsConstructor
public class InventoryRestController {

    private final InventoryServiceImplementation inventoryServiceImplementation;
    private final InventoryService inventoryService;
    private final InventoryUtil inventoryUtil;
    private final StockGroupService stockGroupService;

    @PostMapping("/send-to-tally")
    public ResponseEntity<String> sendInventoryToTally(@RequestBody @Valid InventoryDto inventoryDto, HttpSession session) {
        log.info("Received inventory data for Tally: {}", inventoryDto);
        if (inventoryDto.getHsnCode() == null || inventoryDto.getHsnCode().isEmpty()) {
            return new ResponseEntity<>("HSN Code is mandatory", HttpStatus.BAD_REQUEST);
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return new ResponseEntity<>("Unauthorized: User not logged in", HttpStatus.UNAUTHORIZED);
        }
        log.info("Received inventory data for Tally from user: {}", username);
        String response = inventoryService.sendInventoryToTally(inventoryDto, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<InventoryEntity> findById(@PathVariable Integer id) {
        log.info("Finding inventory by id: {}", id);
        InventoryEntity entity = inventoryServiceImplementation.findById(id);
        if (entity != null) {
            log.info("Found inventory: {}", entity);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } else {
            log.warn("Inventory not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryEntity>> findAll() {
        log.info("Fetching all inventory items.");
        List<InventoryEntity> allItems = inventoryServiceImplementation.findAll();
        log.info("Total inventory items found: {}", allItems.size());
        return new ResponseEntity<>(allItems, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        try {
            inventoryServiceImplementation.deleteById(id);
            log.info("Soft deleted inventory with ID: {}", id);
            return new ResponseEntity<>("Inventory deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during inventory deletion: {}", e.getMessage());
            return new ResponseEntity<>("Failed to delete inventory.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paginate")
    public ResponseEntity<Page<InventoryDto>> getPaginatedInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<InventoryDto> inventoryPage = inventoryServiceImplementation.findPaginated(page, size);
        return new ResponseEntity<>(inventoryPage, HttpStatus.OK);
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getPaginatedInventoryData(@RequestParam(defaultValue = "0") int page) {
        Page<InventoryEntity> inventoryPage = inventoryService.getPaginatedInventory(page, 25);
        List<InventoryDto> inventoryDtos = inventoryPage.getContent().stream()
                .map(inventoryUtil::convertEntityToDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("data", inventoryDtos);
        response.put("count", inventoryDtos.size());
        response.put("totalPages", inventoryPage.getTotalPages());
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetch-inventory-by-group")
    public ResponseEntity<List<InventoryDto>> fetchInventoryByGroup(
            @RequestParam String companyName,
            @RequestParam String groupName) {
        List<InventoryDto> inventoryList = inventoryServiceImplementation.fetchInventoryByGroup(companyName, groupName);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @PostMapping("/update-local")
    public ResponseEntity<String> updateLocalDatabase(@RequestBody InventoryDto inventoryDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return new ResponseEntity<>("Unauthorized: User not logged in", HttpStatus.UNAUTHORIZED);
        }
        try {
            inventoryServiceImplementation.updateLocalDatabase(inventoryDto,username);
            return ResponseEntity.ok("Inventory updated in local database.");
        } catch (Exception e) {
            log.error("Failed to update inventory in local database: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update inventory.");
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> suggestionByMake(@RequestParam String make) {
        List<String> makes = inventoryServiceImplementation.suggestionByMake(make);
        if (makes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(makes);
        }
    }

    @GetMapping("/search-by-make")
    public ResponseEntity<Map<String, Object>> searchByMake(
            @RequestParam String make,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        System.out.println("Invoking searchByMake with make: " + make + ", pageNo: " + pageNo + ", pageSize: " + pageSize);
        Page<InventoryDto> inventoryItems = inventoryServiceImplementation.searchByMake(make, pageNo, pageSize);
        if (inventoryItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", inventoryItems.getContent());
        response.put("currentPage", inventoryItems.getNumber());
        response.put("totalPages", inventoryItems.getTotalPages());
        response.put("totalItems", inventoryItems.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock-group/names")
    public ResponseEntity<List<String>> getStockGroupNames() {
        log.info("Fetching all stock group names");
        List<String> stockGroups = stockGroupService.getAllStockGroupNames();
        return ResponseEntity.ok(stockGroups);
    }

}