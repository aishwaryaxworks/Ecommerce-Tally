package com.netzwerk.ecommerce.controller;

import com.netzwerk.ecommerce.dto.OrderDto;
import com.netzwerk.ecommerce.dto.payment.PaymentDto;
import com.netzwerk.ecommerce.service.order.OrderService;
import com.netzwerk.ecommerce.service.payment.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;
    private OrderService orderService;

    @GetMapping()
    public String displayPaymentForm(@RequestParam("id") int orderId, Model model) {
        OrderDto order = orderService.getOrderDtoById(orderId);
        model.addAttribute("order", order);
        return "payment";
    }

    @PostMapping("/processPayment")
    public String save(@ModelAttribute PaymentDto paymentDto, HttpSession session){
        String username = (String) session.getAttribute("username");
        paymentDto.setCreatedBy(username);
        paymentService.savePayment(paymentDto);
        return  "redirect:/order/view?action=save&status=success";
    }

}
