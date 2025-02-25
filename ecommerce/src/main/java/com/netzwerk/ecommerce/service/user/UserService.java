package com.netzwerk.ecommerce.service.user;

import com.netzwerk.ecommerce.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String register(UserDto userDto);
    String login(UserDto userDto);
    String getLoggedInUsername(UserDto userDto);
    UserDto getUserDetails(String userEmail);
}
