package com.netzwerk.ecommerce.resources;

import com.netzwerk.ecommerce.dto.StockGroupDto;
import com.netzwerk.ecommerce.service.stockGroup.StockGroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stock-group")
@AllArgsConstructor
@Slf4j
public class StockGroupRestController {
    private final StockGroupService stockGroupService;

    @PostMapping("/create")
    public ResponseEntity<String> createStockGroup(@RequestBody StockGroupDto stockGroupDto) {
        if (stockGroupDto.getName() == null || stockGroupDto.getName().trim().isEmpty()) {
            return new ResponseEntity<>("Stock group name is required", HttpStatus.BAD_REQUEST);
        }
        if (stockGroupService.isStockGroupNameExists(stockGroupDto.getName())) {
            return new ResponseEntity<>("Stock group already exists", HttpStatus.BAD_REQUEST);
        }
        stockGroupService.createStockGroup(stockGroupDto);
        return ResponseEntity.ok("Stock group created successfully.");
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getStockGroupNames() {
        log.info("Fetch Stock Group names");
        List<String> stockGroupNames = stockGroupService.getAllStockGroupNames();
        return ResponseEntity.ok(stockGroupNames);
    }

    @GetMapping("/checkByGroupName")
    public String checkByGroupName(@RequestParam("name") String name) {
        if (stockGroupService.isStockGroupNameExists(name)) {
            return "The stock group with name '" + name + "' already exists.";
        }
        return "The stock group with name '" + name + "' does not exist.";
    }
}

