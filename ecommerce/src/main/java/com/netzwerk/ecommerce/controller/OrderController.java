package com.netzwerk.ecommerce.controller;

import com.netzwerk.ecommerce.dto.OrderDto;
import com.netzwerk.ecommerce.service.customer.CustomerService;
import com.netzwerk.ecommerce.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    @GetMapping("/saveOrders")
    public String homeToOrders(Model model) {
        List<String> customerNames = customerService.getAllCustomerNames();
        List<String> productGroupNames = orderService.getDistinctGroupName();
        model.addAttribute("groupNames", productGroupNames);
        model.addAttribute("customerNames", customerNames);
        model.addAttribute("order", new OrderDto());
        log.info("Attempting to display Order details");
        return "order";
    }

    @PostMapping("/save")
    public String saveOrders(@ModelAttribute("order") OrderDto orderDto, HttpSession session) {
        String username = (String) session.getAttribute("username");
        log.info("Saving order with customer name: {} :{}", orderDto, username);
        orderDto.setCreatedBy(username);
        orderService.saveOrder(orderDto);
        return "redirect:/order/view?action=save&status=success";
    }

    @GetMapping("/byVoucherTypeName")
    public String getByVoucherTypeName(@RequestParam("selectedOption") String voucherTypeName,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size, Model model) {
        log.info("Fetching orders by voucher type: {}", voucherTypeName);
        return displayOrderbyVoucherTypeName(voucherTypeName, model, page, size);
    }

    @GetMapping("/view")
    public String viewOrders(Model model) {
        log.info("Fetching all orders (paginated)");
        return displayOrder(model, 0, 25);
    }

    @GetMapping("/display")
    public String displayOrders(@RequestParam(required = false) String voucherTypeName,
                                Model model,
                                @RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        if (voucherTypeName != null && !voucherTypeName.isEmpty()) {
            return displayOrderbyVoucherTypeName(voucherTypeName, model, page, size);
        }
        return displayOrder(model, page, size);
    }

    private String displayOrderbyVoucherTypeName(String voucherTypeName, Model model,
                                                 Integer page, Integer size) {
        int defaultPage = (page == null) ? 0 : page;
        int defaultSize = (size == null) ? 25 : size;
        log.info("Displaying orders by voucher type: {} with page: {} and size: {}", voucherTypeName, defaultPage, defaultSize);
        Page<OrderDto> orders = orderService.getByVoucherType(voucherTypeName, defaultPage, defaultSize);
//        List<String> voucherTypeNam = Arrays.asList("Sales", "Purchase Order");
        model.addAttribute("selectedOptions", voucherTypeName);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", defaultPage);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("pageSize", defaultSize);
        model.addAttribute("totalItems", orders.getTotalElements());
        return "orderView";
    }

    private String displayOrder(Model model, Integer page, Integer size) {
        int defaultPage = (page == null) ? 0 : page;
        int defaultSize = (size == null) ? 25 : size;
        log.info("Displaying all orders with page: {} and size: {}", defaultPage, defaultSize);
        Page<OrderDto> orders = orderService.findPaginated(defaultPage, defaultSize);
        List<String> voucherTypeName = Arrays.asList("Sales", "Purchase Order");
        model.addAttribute("selectedOptions", voucherTypeName);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", defaultPage);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("pageSize", defaultSize);
        model.addAttribute("totalItems", orders.getTotalElements());
        return "orderView";
    }

    @GetMapping("/edit")
    public String viewToUpdate(@RequestParam("id") int id, Model model) {
        OrderDto orderDto = orderService.getOrderDtoById(id);
        List<String> customerNames = customerService.getAllCustomerNames();
        List<String> groupName = orderService.getDistinctGroupName();
        List<String> make = orderService.getDistinctMakes(orderDto.getGroupName());
        List<String> models = orderService.getDistinctModel(orderDto.getGroupName(), orderDto.getMake());
        List<String> productCodes = orderService.getDistinctProductCode(orderDto.getGroupName(), orderDto.getMake(), orderDto.getModel());
        log.info("Retrieved makes from service: {}", make);
        model.addAttribute("customerNames", customerNames);
        model.addAttribute("groupNames", groupName);
        model.addAttribute("makes", make);
        model.addAttribute("models", models);
        model.addAttribute("productCodes", productCodes);
        model.addAttribute("orderDto", orderDto);
        return "orderUpdate";
    }

    @PostMapping("/update")
    public String updateOrder(@ModelAttribute("order") OrderDto order, HttpSession session) {
        String username = (String) session.getAttribute("username");
        order.setUpdatedBy(username);
        orderService.updateOrder(order);
        log.info("Updated Order successfully.{}", order);
        return "redirect:/order/view?action=update&status=success";
    }

    @GetMapping("/view_order")
    public String view(@RequestParam("id") int id, Model model) {
        OrderDto orderDto = orderService.getOrderDtoById(id);
        model.addAttribute("orderDto", orderDto);
        return "o_view";
    }

    @GetMapping("/delete")
    public String deleteOrder(@RequestParam("id") int id) {
        orderService.deleteOrder(id);
        log.info("Delete Order successfully.{}", id);
        return "redirect:/order/view?action=delete&status=success";
    }
    @GetMapping("/approve")
    public String approveOrder(@RequestParam("id") int orderId, HttpSession session) {
        OrderDto order = orderService.getOrderDtoById(orderId);
        order.setOrderStatus(true); // ✅ Set status to Approved
        return updateOrder(order, session); // ✅ Reuse updateOrder()
    }

}

