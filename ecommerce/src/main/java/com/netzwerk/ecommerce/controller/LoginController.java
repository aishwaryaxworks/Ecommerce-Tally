package com.netzwerk.ecommerce.controller;

import com.netzwerk.ecommerce.dto.UserDto;
import com.netzwerk.ecommerce.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j

@RequestMapping("/")

public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDto());
        log.info("Login page");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserDto userDto, Model model, HttpSession session) {
        String result = userService.login(userDto);
        if (result.contains("Welcome")) {
          UserDto dto =  userService.getUserDetails(userDto.getEmail());
          if(dto!=null) {
              session.setAttribute("username", dto.getUsername());
              session.setAttribute("role", dto.getUserRole());
              return "redirect:/index";
          }

        }
        model.addAttribute("error", result);
        return "login";

    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        log.info("New user registration");
        return "register";
    }

    @PostMapping("/loginregister")
    public String register(@ModelAttribute("user") UserDto userDto, Model model) {
        String result = userService.register(userDto);
        if (result.equals("Registration successful")) {
            model.addAttribute("message", result);
            log.info("Redirect to login after new registration is completed");
            return "redirect:/login";
        } else {
            model.addAttribute("error", result);
            log.info("Stay on same page as registration has failed");
            return "register";
        }
    }
}
