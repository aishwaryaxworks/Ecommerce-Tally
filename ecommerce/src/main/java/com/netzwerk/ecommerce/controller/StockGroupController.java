package com.netzwerk.ecommerce.controller;

import com.netzwerk.ecommerce.dto.StockGroupDto;
import com.netzwerk.ecommerce.service.stockGroup.StockGroupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stock-group")
@AllArgsConstructor
@Slf4j
public class StockGroupController {

    private final StockGroupService stockGroupService;

    @PostMapping("/create")
    public String createStockGroup(@ModelAttribute("stockGroupDto") @Valid StockGroupDto stockGroupDto,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "inventory";
        }
        stockGroupService.createStockGroup(stockGroupDto);
        return "redirect:/inventory";
    }
}
