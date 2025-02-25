package com.netzwerk.ecommerce.service.tally;

import com.netzwerk.ecommerce.dto.CustomerDetailsDto;
import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "tally", url = "${tallyapplication.url}")
public interface TallyInterface {

    @PostMapping("/inventory")
    String save(InventoryDto dto);

    @PostMapping("/customer")
    String save(CustomerDetailsDto dto);

    @GetMapping("/inventory/fetch")
    List<InventoryDto> fetchInventory(@RequestParam String companyName);

    @GetMapping("/inventory/fetch-by-group")
    List<InventoryDto> fetchInventoryByGroup(
            @RequestParam String companyName,
            @RequestParam String groupName);

    @GetMapping("/inventory/fetch-stock-items")
    List<InventoryDto> fetchStockItems(@RequestParam String companyName, @RequestParam String stockName);

    @PostMapping("/order")
    String save(OrderDto dto);
}
