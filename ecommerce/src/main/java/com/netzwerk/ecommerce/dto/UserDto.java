package com.netzwerk.ecommerce.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Component
@Getter
@Setter
public class UserDto {
    private String email;
    private String password;
    private String username;
    private String userRole;
}
