package com.netzwerk.ecommerce.controller;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.entity.InventoryEntity;
import com.netzwerk.ecommerce.service.inventory.InventoryService;
import com.netzwerk.ecommerce.service.inventory.InventoryServiceImplementation;
import com.netzwerk.ecommerce.service.stockGroup.StockGroupService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inventory")
@Slf4j
@AllArgsConstructor
public class InventoryController {

    private final InventoryServiceImplementation inventoryServiceImplementation;
    private final InventoryService inventoryService;
    private final StockGroupService stockGroupService;

    @GetMapping
    public String showInventoryPage(Model model) {
        model.addAttribute("inventory", new InventoryDto());
        model.addAttribute("stockGroups", stockGroupService.getAllStockGroupNames());
        log.info("Loading add dto page with stock groups");
        return "inventory";
    }

    @PostMapping("/send-to-tally")
    public String saveDevice(@ModelAttribute("inventory") @Valid InventoryDto inventoryDto, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            log.info("Validation errors occurred: {}", bindingResult.getAllErrors());
            return "inventory";
        }
        String duplicateCheckMessage = inventoryServiceImplementation.checkForDuplicateInventory(
                inventoryDto.getGroupName(),
                inventoryDto.getSubGroupName(),
                inventoryDto.getProductCode());
        if (!duplicateCheckMessage.isEmpty()) {
            bindingResult.rejectValue("productCode", "error.inventoryDto", duplicateCheckMessage);
            log.info("Duplicate check failed: {}", duplicateCheckMessage);
            return "inventory";
        }
        String username = (String) session.getAttribute("username");
        inventoryServiceImplementation.sendInventoryToTally(inventoryDto, username);
        log.info("Saving inventory & displaying");
        return "redirect:/inventory/display?action=save&status=success";
    }

    @GetMapping("/viewInventory")
    public String showInventory(Model model) {
        log.info("Fetching customer list");
        return displayInventory(model, 0, 25);
    }

    @GetMapping("/display")
    public String displayInventory(Model model,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size) {
        int defaultPage = (page == null) ? 0 : page;
        int defaultSize = (size == null) ? 25 : size;
        Page<InventoryDto> inventoryPage = inventoryServiceImplementation.findPaginated(defaultPage, defaultSize);
        model.addAttribute("inventoryPage", inventoryPage);
        model.addAttribute("currentPage", defaultPage);
        model.addAttribute("totalPages", inventoryPage.getTotalPages());
        model.addAttribute("pageSize", defaultSize);
        model.addAttribute("totalItems", inventoryPage.getTotalElements());
        return "inventoryList";
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryEntity> findById(@PathVariable Integer id) {
        InventoryEntity entity = inventoryServiceImplementation.findById(id);
        log.info("Finding product by id : {}", id);
        return entity != null ? new ResponseEntity<>(entity, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryEntity>> findAll() {
        List<InventoryEntity> allItems = inventoryServiceImplementation.findAll();
        log.info("Listing all products");
        return new ResponseEntity<>(allItems, HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    public String showEditInventoryPage(@PathVariable Integer id, Model model) {
        InventoryEntity inventoryEntity = inventoryServiceImplementation.findById(id);
        if (inventoryEntity != null) {
            log.info("Converting entity to dto");
            InventoryDto inventoryDto = inventoryServiceImplementation.convertEntityToDto(inventoryEntity);
            model.addAttribute("inventory", inventoryDto);
            model.addAttribute("stockGroups", stockGroupService.getAllStockGroupNames());
            log.info("Editing inventory with id: {}", id);
            return "inventoryEdit";
        }
        log.info("Redirecting to display page as inventory not found");
        return "redirect:/inventory/display";
    }

    @PostMapping("/update")
    public String updateInventory(@ModelAttribute("inventory") InventoryDto inventoryDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        inventoryServiceImplementation.updateLocalDatabase(inventoryDto, username);
        log.info("Updated inventory");
        return "redirect:/inventory/display?action=update&status=success";
    }

    @GetMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Integer id) {
        log.info("Received request to delete inventory with ID: {}", id);
        try {
            inventoryServiceImplementation.deleteById(id);
            log.info("Successfully deleted inventory with ID: {}", id);
            return "redirect:/inventory/display?action=delete&status=success";
        } catch (Exception e) {
            log.error("Error occurred while deleting inventory with ID {}: {}", id, e.getMessage());
            return "redirect:/inventory/display?action=delete&status=failure";
        }
    }

    @GetMapping("/api/fetch-from-tally")
    public List<InventoryDto> fetchInventoryFromTally(@RequestParam String companyName) {
        return inventoryServiceImplementation.fetchInventoryFromTally(companyName);
    }

    @GetMapping("/search")
    public String searchByMakeModuleAndProductCode(@RequestParam String make, @RequestParam String model, @RequestParam String productCode, Model models) {
        log.info("Search by email");
        InventoryDto inventoryDto = inventoryServiceImplementation.findByMakeModuleAndProductCode(make, model, productCode);
        if (inventoryDto != null) {
            models.addAttribute("inventoryDto", inventoryDto);
        } else {
            models.addAttribute("errorMessage", "inventory details not found for product code: " + inventoryDto);
        }
        return "redirect:/inventory/display";
    }

}
