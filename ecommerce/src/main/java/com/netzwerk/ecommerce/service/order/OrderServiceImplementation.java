package com.netzwerk.ecommerce.service.order;

import com.netzwerk.ecommerce.dto.*;
import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.entity.CustomerDetailsEntity;
import com.netzwerk.ecommerce.entity.InventoryEntity;
import com.netzwerk.ecommerce.entity.OrderEntity;
import com.netzwerk.ecommerce.repository.*;
import com.netzwerk.ecommerce.repository.InventoryRepository;
import com.netzwerk.ecommerce.service.customer.CustomerService;
import com.netzwerk.ecommerce.service.tally.TallyInterface;
import com.netzwerk.ecommerce.util.CustomerUtil;
import com.netzwerk.ecommerce.util.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImplementation implements OrderService {
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private OrderEntity entity;
    private InventoryDto inventoryDto;
    private CustomerDetailsRepository customerDetailsRepository;
    private final TallyInterface tallyInterface;
    private CustomerService customerService;

    @Override
    public List<String> getDistinctGroupName() {
        List<String> group = inventoryRepository.findDistinctGroupName();
        log.info("Found {} distinct Group.", group);
        return group;
    }

    @Override
    public List<String> getDistinctMakes(String productGroupName) {
        List<String> makes = inventoryRepository.findDistinctMakes(productGroupName);
        log.info("Found {} distinct makes.", makes.size());
        return makes;
    }

    @Override
    public List<String> getDistinctModel(String productGroupName, String make) {
        log.info("Invoking getDistinctModel method for make: {}", make);
        List<String> models = inventoryRepository.findDistinctModelsByMake(productGroupName, make);
        log.info("Found {} distinct models for make: {}", models.size(), make);
        return models;
    }

    @Override
    public List<String> getDistinctProductCode(String productGroupName, String make, String model) {
        log.info("Invoking getDistinctProductCode method for make: {}, model: {}", make, model);
        List<String> productCodes = inventoryRepository.findDistinctProductCodesByMakeAndModel(productGroupName, make, model);
        log.info("Found {} distinct product codes for make: {}, model: {}", productCodes.size(), make, model);
        return productCodes;
    }

    @Override
    public InventoryDto getPriceAndStockInHand(String productGroupName, String make, String model, String pCode) {
        log.info("Invoking getPriceAndStockInHand method for make: {}, model: {}, product code: {}", make, model, pCode);
        InventoryEntity inventoryEntity = inventoryRepository.findByGroupNameMakeModelAndProductCode(productGroupName, make, model, pCode);
        if (inventoryEntity != null) {
            log.info("rate stock" + inventoryEntity.getRate() + "" + inventoryEntity.getOpeningValue());
            BeanUtils.copyProperties(inventoryEntity, inventoryDto);
            return inventoryDto;
        } else {
            log.warn("No data found for make: {}, model: {}, product code: {}", make, model, pCode);
            return null;
        }
    }
   @Override
    public void saveOrder(OrderDto order) {
        if (order == null || order.getModel() == null || order.getMake() == null) {
            log.error("Failed to save order: Order object is null or missing required fields.");
            return;
        }
        OrderEntity entity = new OrderEntity();
        order.setCreatedOn(LocalDateTime.now());
        BeanUtils.copyProperties(order, entity);
       updateStockInHand(order.getMake(), order.getModel(), order.getProductCode(), order.getQuantity());
        customerDetailsRepository.findById(order.getCustomerId()).ifPresent(customer -> {
            entity.setCustomerDetailsEntity(customer);
            customer.getOrders().add(entity);
            if ("Sales".equalsIgnoreCase(order.getVoucherTypeName())) {
                customer.setCustomerPayableAmount(
                        (customer.getCustomerPayableAmount() != null ? customer.getCustomerPayableAmount() : 0) +
                                (order.getTotalPrice() != null ? order.getTotalPrice() : 0)
                );
            }
            orderRepository.save(entity);
            customerDetailsRepository.save(customer);
        });

        log.info("Order saved successfully with ID: {}", order.getProductCode());
    }

    public void updateStockInHand(String make, String model, String pCode, Integer quantityDifference) {
        InventoryEntity entity = inventoryRepository.findByMakeModelAndProductCode(make, model, pCode);
        if (entity != null) {
            log.debug("Existing Inventory found with ID: {}. Proceeding with update.", entity.getId());
            int updatedStockInHand = entity.getOpeningBalance() - quantityDifference;
            try {
                if (updatedStockInHand < 0) {
                    log.error("Insufficient stock for Product Code: {}. Current Stock: {}, Quantity Change: {}", pCode, entity.getOpeningBalance(), quantityDifference);
                }
            } catch (IllegalStateException e) {
                log.error("Insufficient stock for the requested order:{}", e.getMessage());
            }
            entity.setOpeningBalance(updatedStockInHand);
            inventoryRepository.save(entity);
            log.info("Stock In Hand updated successfully for ID: {}. Updated stock: {}", entity.getId(), updatedStockInHand);
        } else {
            log.error("Failed to update stock: No inventory found for Make: {}, Model: {}, Product Code: {}", make, model, pCode);
        }
    }

    @Override
    public Page<OrderDto> findPaginated(int pageNo, int pageSize) {
        log.info("Starting pagination for pageNo: {}, pageSize: {}", pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        log.debug("Created Pageable object: {}", pageable);
        Page<OrderEntity> orderPage = orderRepository.findByIsActiveTrueOrderByIdDesc(pageable);
        log.info("Fetched {} records from database", orderPage.getTotalElements());
        List<OrderDto> orderDtos = orderPage.stream().map(OrderUtil::convertToDto).collect(Collectors.toList());
        log.info("Finding Order details and adding pagination after converting entity to dto");
        log.debug("Converted OrderEntity to OrderDto, total records: {}", orderDtos.size());
        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public Page<OrderDto> getByVoucherType(String voucherTypeName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<OrderEntity> orderEntities = orderRepository.findByVoucherType(voucherTypeName, pageable);
        List<OrderDto> orderDtos = orderEntities.stream().map(OrderUtil::convertToDto).collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orderEntities.getTotalPages());
    }

    @Override
    public OrderDto getOrderDtoById(int id) {
        OrderDto dto = new OrderDto();
        OrderEntity entity = orderRepository.getById(id);
        log.info("getOrderDtoById "+entity.getTotalPrice());
        if (Boolean.TRUE.equals(entity.getIsActive())) {
            BeanUtils.copyProperties(orderRepository.getById(id), dto);
            log.info("Order data : " + dto.getMake() + "," + dto.getModel() + "," + dto.getProductCode());
            return dto;
        }
        return dto;
    }

    @Override
    public String updateOrder(OrderDto updatedOrder) {
        Optional<OrderEntity> existingOrderOptional = orderRepository.findById(updatedOrder.getId());
        if (existingOrderOptional.isPresent()) {
            OrderEntity existingOrder = existingOrderOptional.get();
            int quantityDifference = updatedOrder.getQuantity() - existingOrder.getQuantity();
//            updateStockInHand(updatedOrder.getMake(), updatedOrder.getModel(), updatedOrder.getProductCode(), quantityDifference);
            OrderEntity updatedOrderEntity = getEntity(updatedOrder, Optional.of(existingOrder));

            orderRepository.save(updatedOrderEntity);
            log.info("Order updated successfully with ID: {}", updatedOrder.getId());
            return "Order Updated successfully: " + updatedOrder.getProductCode();
        } else {
            log.error("Failed to update order: No existing order found with ID: {}", updatedOrder.getId());
            return "Failed to update order.";
        }
    }

    private static OrderEntity getEntity(OrderDto updatedOrder, Optional<OrderEntity> existingOrder) {
        OrderEntity editOrder = existingOrder.get();
        BeanUtils.copyProperties(updatedOrder, editOrder);
        editOrder.setUpdatedOn(LocalDateTime.now());
        return editOrder;
    }

    @Override
    public String deleteOrder(int id) {
        OrderEntity orderToDelete = orderRepository.getById(id);
        if (orderToDelete != null) {
            log.debug("Existing order found with ID: {}. Proceeding with delete.", id);
            orderToDelete.setIsActive(false);
            orderRepository.save(orderToDelete);
            log.info("Order deleted successfully with ID: {}", id);
            return "Order deleted successfully: " + orderToDelete.getProductCode();
        } else {
            log.error("Failed to Delete order: No existing order found with ID: {}", orderToDelete.getId());
            return "Failed to Delete order ";
        }
    }

    @Override
    public List<String> getCustomerName() {
        return customerDetailsRepository.getCustomerName();
    }

    @Override
    public List<OrderDto> getSuggestions(String customerName) {
        List<OrderEntity> orderEntities = orderRepository.suggestionByCustomerName(customerName);
        if (orderEntities != null) {
            return orderEntities.stream().map(OrderUtil::convertToDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<OrderDto> getByVoucherTypeName(String voucherTypeName) {
        if (voucherTypeName != null) {
         List<OrderEntity> orderEntities =orderRepository.findByVoucherType(voucherTypeName);
          return   orderEntities.stream().map(OrderUtil::convertToDto).collect(Collectors.toList());
        }
        return List.of();
    }

}
