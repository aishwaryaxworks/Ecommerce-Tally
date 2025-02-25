package com.netzwerk.ecommerce.util;

import com.netzwerk.ecommerce.constant.ServiceConstant;
import com.netzwerk.ecommerce.dto.CustomerDetailsDto;
import com.netzwerk.ecommerce.entity.CustomerDetailsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;


@Slf4j
public class CustomerUtil {
    private CustomerUtil() {

    }
    public static void assignValuesToCustomerDetails(CustomerDetailsDto customerDetailsDto) {
        if(customerDetailsDto.getAddress2()==null){
            customerDetailsDto.setAddress2(ServiceConstant.NA.toString());
        }
        if(customerDetailsDto.getAddress3()==null){
            customerDetailsDto.setAddress3(ServiceConstant.NA.toString());
        }
    }

    public static CustomerDetailsDto  customerEntityToDto(CustomerDetailsEntity entity){
        CustomerDetailsDto dto= new CustomerDetailsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public static CustomerDetailsEntity  customerDtoToEntity(CustomerDetailsDto dto){
        CustomerDetailsEntity entity= new CustomerDetailsEntity();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }
}
