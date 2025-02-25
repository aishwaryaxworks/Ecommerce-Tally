package com.netzwerk.ecommerce.service.order;

import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.dto.OrderDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    List<String> getDistinctGroupName();

    List<String> getDistinctMakes(String productGroupName);

    List<String> getDistinctModel(String productGroupName,String make);

    List<String> getDistinctProductCode(String productGroupName,String make, String model);

    InventoryDto getPriceAndStockInHand(String productGroupName,String make, String model, String pCode);

    void saveOrder(OrderDto order);

    Page<OrderDto> findPaginated(int pageNo, int pageSize);

    OrderDto getOrderDtoById(int id);

    String updateOrder(OrderDto orderDto);

    String deleteOrder(int id);

    List<String> getCustomerName();

    List<OrderDto>  getSuggestions(String customerName);

    List<OrderDto> getByVoucherTypeName(String voucherTypeName);

    Page<OrderDto> getByVoucherType(String voucherTypeName,int pageNo, int pageSize);
}
