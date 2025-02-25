package com.netzwerk.ecommerce.util;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.netzwerk.ecommerce.dto.InventoryDto;
import com.netzwerk.ecommerce.dto.UserDto;
import com.netzwerk.ecommerce.entity.UserEntity;
import org.springframework.beans.BeanUtils;


public class ServiceUtil {

    private ServiceUtil() {

    }

    public static UserDto convertUserEntityToDto(UserEntity entity) {
        UserDto dto =  new UserDto();
        if (entity == null) {
            return null;
        }
        BeanUtils.copyProperties(entity, dto);
        return dto;

    }
    public static  UserEntity convertUserDtoToEntity(UserDto dto) {
        UserEntity entity =  new UserEntity();
        if (dto == null) {
            return null;
        }
        BeanUtils.copyProperties(dto,entity);
        return entity;

    }


}
