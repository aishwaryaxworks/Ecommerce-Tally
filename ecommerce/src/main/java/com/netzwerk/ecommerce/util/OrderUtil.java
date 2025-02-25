package com.netzwerk.ecommerce.util;


import com.netzwerk.ecommerce.dto.OrderDto;
import com.netzwerk.ecommerce.entity.OrderEntity;
import org.springframework.beans.BeanUtils;

public class OrderUtil {

    private OrderUtil(){

    }
    public static OrderDto convertToDto(OrderEntity orderEntity) {
        OrderDto dto = new OrderDto();
        BeanUtils.copyProperties(orderEntity, dto);
        dto.setCustomerName(orderEntity.getCustomerName());
        return dto;
    }

}
