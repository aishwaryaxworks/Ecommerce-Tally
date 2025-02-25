package com.netzwerk.ecommerce.resources;

import com.netzwerk.ecommerce.dto.CustomerDetailsDto;
import com.netzwerk.ecommerce.service.customer.CustomerServiceImplementation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Slf4j
@AllArgsConstructor
public class CustomerRestController {

    private final CustomerServiceImplementation customerServiceImplementation;

    @GetMapping("/email")
    public String checkCustomerEmail(@RequestParam String customerEmail) {
        log.info("Checking customer email: {}", customerEmail);
        String result = customerServiceImplementation.checkCustomerEmail(customerEmail);
        log.info("Result for customer email '{}': {}", customerEmail, result);
        return result;
    }

    @GetMapping("/contactNumber")
    public String checkCustomerContactNumber(@RequestParam String contactNumber) {
        log.info("Checking customer contact number: {}", contactNumber);
        String result = customerServiceImplementation.checkCustomerContactNumber(contactNumber);
        log.info("Result for contact number '{}': {}", contactNumber, result);
        return result;
    }

    @GetMapping("/customerGstNumber")
    public String checkCustomerGstNumber(@RequestParam String customerGstNumber) {
        log.info("Checking customer GST number: {}", customerGstNumber);
        String result = customerServiceImplementation.checkCustomerGstNumber(customerGstNumber);
        log.info("Result for GST number '{}': {}", customerGstNumber, result);
        return result;
    }

    @GetMapping("/checkCustomerName")
    public String checkCustomer(@RequestParam String customerName){
        log.info("checking the customer name:{} ",customerName);
        return customerServiceImplementation.checkByCustomerName(customerName);
    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<String> saveCustomerDetails(@RequestBody CustomerDetailsDto customerDetailsDto) {
        log.info("Saving the customer details: {}", customerDetailsDto);
        customerServiceImplementation.onSave(customerDetailsDto);
        log.info("Customer details saved successfully: {}", customerDetailsDto);
        return new ResponseEntity<>("Customer details saved successfully", HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CustomerDetailsDto> viewToUpdate(@PathVariable Integer id) {
        log.info("Fetching customer details for ID: {}", id);
        CustomerDetailsDto customer = customerServiceImplementation.getCustomerDetails(id);
        if (customer != null) {
            log.info("Found inventory: {}", customer);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            log.warn("Inventory not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/display")
    public ResponseEntity<Page<CustomerDetailsDto>> displayCustomer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Displaying customer inventory - Page: {}, Size: {}", page, size);
        Page<CustomerDetailsDto> customerList = customerServiceImplementation.findPaginated(page, size);
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDetailsDto customerDetailsDto, Model model) {
        log.info("Updating customer details: {}", customerDetailsDto);
        String msg = customerServiceImplementation.updateCustomer(customerDetailsDto);
        log.info("Updated customer successfully: {}", customerDetailsDto);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        log.info("Deleting customer with ID: {}", id);
        String msg = customerServiceImplementation.deleteCustomer(id);
        log.info("Deleted customer successfully with ID: {}", id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/city/{country}/{state}")
    public List<String> getAllCity(
            @PathVariable("country") String country,
            @PathVariable("state") String state) {
        return customerServiceImplementation.getAllCities(country, state);
    }

    @GetMapping("/suggestions")
    public List<CustomerDetailsDto> suggestionByName(@RequestParam("customerName") String customerName) {
        if (customerName == null || customerName.isBlank()) {
            return Collections.emptyList();
        }
        List<CustomerDetailsDto> customers = customerServiceImplementation.suggestionByName(customerName);
        return customers.isEmpty() ? Collections.emptyList() : customers;
    }

    @GetMapping("/search")
    public ResponseEntity<CustomerDetailsDto> searchCustomerByEmail(@RequestParam("customerEmailId") String customerEmailId) {
        CustomerDetailsDto customerDetails = customerServiceImplementation.findByEmail(customerEmailId);
        if (customerDetails != null) {
            return ResponseEntity.ok(customerDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/byCustomerName")
    public CustomerDetailsDto getByCustomerName(@RequestParam String customerName){
        return customerServiceImplementation.findByCustomerName(customerName);
    }

}
