package com.netzwerk.ecommerce.controller;

import com.netzwerk.ecommerce.dto.CustomerDetailsDto;
import com.netzwerk.ecommerce.dto.RegionDTO;
import com.netzwerk.ecommerce.service.customer.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/customer")
@Controller
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/getCustomerDetails")
    public String homeToCustomer(Model model) {
        model.addAttribute("customer", new CustomerDetailsDto());
        log.info("Navigated to customer details page.");
        return "customer";
    }

    @PostMapping("/saveCustomer")
    public String saveCustomerDetails(@ModelAttribute CustomerDetailsDto customerDetailsDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        log.info("Saving the customer details: {}", customerDetailsDto);
        customerDetailsDto.setCreatedBy(username);
        customerService.onSave(customerDetailsDto);
        log.info("Customer details saved successfully: {}", customerDetailsDto);
        return "redirect:/customer/toView?action=save&status=success";
    }

    @GetMapping("/toView")
    public String showCustomerList(Model model) {
        log.info("Fetching customer list");
        return displayCustomer(model, 0, 25);
    }

    @GetMapping("/display")
    public String displayCustomer(Model model,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size) {
        int defaultPage = (page == null) ? 0 : page;
        int defaultSize = (size == null) ? 25 : size;
        Page<CustomerDetailsDto> customerList = customerService.findPaginated(defaultPage, defaultSize);
        model.addAttribute("customerList", customerList.getContent());
        model.addAttribute("currentPage", defaultPage);
        model.addAttribute("totalPages", customerList.getTotalPages());
        model.addAttribute("pageSize", defaultSize);
        model.addAttribute("totalItems", customerList.getTotalElements());
        return "customerList";
    }

    @GetMapping("/edit")
    public String viewToUpdate(@RequestParam("id") int id, Model model) {
        log.info("Fetching customer details for ID: {}", id);
        CustomerDetailsDto customer = customerService.getCustomerDetails(id);
        List<RegionDTO> states=  customerService.getAllStates(customer.getCountry());
        List<String> cities=customerService.getAllCities(customer.getCountry(),customer.getState());
        log.info("customer info "+customer);
        model.addAttribute("customer", customer);
        model.addAttribute("states",states);
        model.addAttribute("cities",cities);
        return "customerUpdate";
    }

    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customer") CustomerDetailsDto customerDetailsDto,Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        log.info("Updating customer details: {}", customerDetailsDto);
        customerDetailsDto.setUpdatedBy(username);
        String msg=customerService.updateCustomer(customerDetailsDto);
        model.addAttribute("message",msg);
        log.info("Updated customer successfully: {}", customerDetailsDto);
        return "redirect:/customer/toView?action=update&status=success";
    }

    @GetMapping("/delete")
    public String deleteCustomer(@RequestParam("id") int id,Model model) {
        log.info("Deleting customer with ID: {}", id);
        String msg=customerService.deleteCustomer(id);
        model.addAttribute("message",msg);
        log.info("Deleted customer successfully with ID: {}", id);
        return "redirect:/customer/toView?action=delete&status=success";
    }

    @GetMapping("/search")
    public String seachByEmail(@RequestParam("customerName") String customerName,Model model){
        log.info("Search by email");
        List<CustomerDetailsDto> customerDetailsDto = customerService.suggestionByName(customerName);
        if (customerDetailsDto != null) {
            model.addAttribute("customer", customerDetailsDto);
        } else {
            model.addAttribute("errorMessage", "Customer not found for email: " + customerName);
        }
        return "redirect:/customer/toView";
    }

}
