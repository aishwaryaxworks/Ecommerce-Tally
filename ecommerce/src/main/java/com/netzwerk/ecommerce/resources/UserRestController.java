package com.netzwerk.ecommerce.resources;

import com.netzwerk.ecommerce.dto.UserDto;
import com.netzwerk.ecommerce.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserRestController {

    private UserService userService;

    @PostMapping()
    public String registerUser(@RequestBody UserDto userDto){
        log.info("user controller:{}",userDto);
        return userService.register(userDto);
    }
    @GetMapping()
    public UserDto getByEmail(@RequestParam String userEmail){
        log.info("user email is:{}",userEmail);
        return userService.getUserDetails(userEmail);
    }

}
