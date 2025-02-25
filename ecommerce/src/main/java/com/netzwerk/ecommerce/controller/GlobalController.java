package com.netzwerk.ecommerce.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GlobalController {

    @ModelAttribute("username")
    public String addUsernameToModel(HttpSession session) {
        return (String) session.getAttribute("username");
    }
}
