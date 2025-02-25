package com.netzwerk.ecommerce.resources;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.dto.OrderDto;
import com.netzwerk.ecommerce.service.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/make/{groupName}")
    public List<String> getMake(@PathVariable("groupName") String groupName) {
        log.debug("Received request to get groupName: {}", groupName);
        List<String> make = orderService.getDistinctMakes(groupName);
        log.info("Returning models for make '{}': {}", groupName, make);
        return make;
    }

    @GetMapping("/models/{groupName}/{make}")
    public List<String> getModel(@PathVariable("groupName") String groupName, @PathVariable("make") String make) {
        log.debug("Received request to get models for make: {}", make);
        List<String> models = orderService.getDistinctModel(groupName, make);
        log.info("Returning models for make '{}': {}", make, models);
        return models;
    }

    @GetMapping("/productCodes/{groupName}/{make}/{model}")
    public List<String> getProductCode(@PathVariable("groupName") String groupName, @PathVariable("make") String make, @PathVariable("model") String model) {
        log.debug("Received request to get product codes for make: '{}' and model: '{}'", make, model);
        List<String> productCodes = orderService.getDistinctProductCode(groupName, make, model);
        log.info("Returning product codes for make '{}' and model '{}': {}", make, model, productCodes);
        return productCodes;
    }

    @GetMapping("/inventory/{groupName}/{make}/{model}/{productCode}")
    public InventoryDto getPriceAndStock(
            @PathVariable("groupName") String groupName,
            @PathVariable("make") String make,
            @PathVariable("model") String model,
            @PathVariable("productCode") String productCode) {
        log.debug("Received request to get price and stock for make: '{}', model: '{}', productCode: '{}'", make, model, productCode);
        InventoryDto inventory = orderService.getPriceAndStockInHand(groupName,make, model, productCode);
        System.out.println("inventory:"+inventory);
        log.info("Returning price and stock information for make: '{}', model: '{}', productCode: '{}': {}", make, model, productCode, inventory);
        return inventory;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveOrders(@RequestBody OrderDto orderDto) {
        log.info("Saving order with customer name: {}", orderDto.getCustomerName());
        orderService.saveOrder(orderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/display")
    public  ResponseEntity<Page<OrderDto>>  displayOrder(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Page<OrderDto> orders= orderService.findPaginated(page, size);
        log.info("Arranging pagination for order details");
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<OrderDto> viewToUpdate(@PathVariable Integer id){
        OrderDto orderDto=orderService.getOrderDtoById(id);
        if (orderDto != null) {
            return new ResponseEntity<>(orderDto,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateOrder(@RequestBody OrderDto order){
        String update=orderService.updateOrder(order);
        return new ResponseEntity<>(update,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id){
        String delete=orderService.deleteOrder(id);
        log.info("Delete Order successfully.{}",id);
        return new ResponseEntity<>(delete,HttpStatus.OK);
    }

    @GetMapping("/suggestions")
    public List<OrderDto>  getSuggestions(@RequestParam String customerName){
        return orderService.getSuggestions(customerName);
    }
    @GetMapping("/byVoucherTypeName")
    public List<OrderDto> getByVoucherTypeName(@RequestParam String voucherTypeName){
        log.info("by voucher type :{}",voucherTypeName);
        return orderService.getByVoucherTypeName(voucherTypeName);
    }

}
