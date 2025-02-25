package com.netzwerk.ecommerce.service.customer;

import com.netzwerk.ecommerce.dto.*;
import com.netzwerk.ecommerce.entity.CustomerDetailsEntity;
import com.netzwerk.ecommerce.repository.CustomerDetailsRepository;
import com.netzwerk.ecommerce.service.tally.TallyInterface;
import com.netzwerk.ecommerce.util.CustomerUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerDetailsRepository customerDetailsRepository;

    private final TallyInterface tallyInterface;


    @Override
    public String onSave(CustomerDetailsDto customerDetailsDto) {
         CustomerDetailsEntity entity=new CustomerDetailsEntity();
        if (customerDetailsDto != null) {
            CustomerUtil.assignValuesToCustomerDetails(customerDetailsDto);
            BeanUtils.copyProperties(customerDetailsDto, entity);
            log.info("After assignment: {}", customerDetailsDto);
            entity.setGuid("NA");
            entity.setCreatedOn(LocalDateTime.now());
            customerDetailsRepository.save(entity);
            log.info("Customer details saved successfully for ID: {}", customerDetailsDto.getCustomerId());
//            tallyInterface.save(customerDetailsDto);
            return "saved";
        } else {
            log.warn("CustomerDetailsDto is null. No data to save.");
            return "not saved";
        }
    }


    public String checkCustomerEmail(String customerEmail) {
        log.info("Checking for duplicate email: {}", customerEmail);
        if (customerDetailsRepository.checkCustomerEmail(customerEmail)) {
            log.warn("Email ID already exists: {}", customerEmail);
            return "Email Id Already Exist";
        }
        return "";
    }

    public String checkCustomerContactNumber(String contactNumber) {
        log.info("Checking for duplicate contact number: {}", contactNumber);
        if (customerDetailsRepository.checkCustomerContactNumber(contactNumber)) {
            log.warn("Contact number already exists: {}", contactNumber);
            return "Contact Already Exist";
        }
        return "";
    }

    public String checkCustomerGstNumber(String gstNumber) {
        log.info("Checking for duplicate GST number: {}", gstNumber);
        if (customerDetailsRepository.checkCustomerGstNumber(gstNumber)) {
            log.warn("GST number already exists: {}", gstNumber);
            return "GST Number Already Exist";
        }
        return "";
    }

    @Override
    public Page<CustomerDetailsDto> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        log.info("Pagination for customer details in descending order of id");
        Page<CustomerDetailsEntity> customerPage = customerDetailsRepository.findByIsActiveTrueOrderByCustomerIdDesc(pageable);
        List<CustomerDetailsDto> customerDetailsDtos = customerPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Fetched paginated customer data - Page: {}, Size: {}", pageNo, pageSize);
        return new PageImpl<>(customerDetailsDtos, pageable, customerPage.getTotalElements());
    }

    @Override
    public CustomerDetailsDto getCustomerDetails(int id) {
        log.info("Fetching customer details for ID: {}", id);
        CustomerDetailsDto dto = new CustomerDetailsDto();
        Optional<CustomerDetailsEntity> entityOpt = customerDetailsRepository.findById(id);
        if (entityOpt.isPresent()) {
            CustomerDetailsEntity entity = entityOpt.get();
            if (entity.getIsActive()) {
                BeanUtils.copyProperties(entity, dto);
                log.info("Fetched customer details successfully for ID: {}", id);
                return dto;
            } else {
                log.warn("Customer with ID: {} is not active", id);
            }
        } else {
            log.error("No customer found with ID: {}", id);
        }
        return dto;
    }

    @Override
    public String updateCustomer(CustomerDetailsDto customerDetailsDto) {
        log.info("Updating customer details: {}", customerDetailsDto);
        Optional<CustomerDetailsEntity> existingCustomer = customerDetailsRepository.findById(customerDetailsDto.getCustomerId());
        if (existingCustomer.isPresent()) {
            extracted(customerDetailsDto, existingCustomer);
//            tallyInterface.save(customerDetailsDto);
            log.info("Customer updated successfully with ID: {}", customerDetailsDto.getCustomerId());
            return "Customer Updated successfully: " + customerDetailsDto.getCustomerName();
        } else {
            log.error("Failed to update customer: No existing customer found with ID: {}", customerDetailsDto.getCustomerId());
            return "Failed to update customer";
        }
    }

    private void extracted(CustomerDetailsDto customerDetailsDto, Optional<CustomerDetailsEntity> existingCustomer) {
        CustomerDetailsEntity editCustomer = existingCustomer.get();
        BeanUtils.copyProperties(customerDetailsDto, editCustomer);
        editCustomer.setUpdatedOn(LocalDateTime.now());
        customerDetailsRepository.save(editCustomer);
        log.info("Updated customer details for ID: {}", editCustomer.getCustomerId());
    }

    @Override
    public String deleteCustomer(int id) {
        log.info("Deleting customer with ID: {}", id);
        Optional<CustomerDetailsEntity> existingCustomer = customerDetailsRepository.findById(id);
        if (existingCustomer.isPresent()) {
            CustomerDetailsEntity deleteCustomer = existingCustomer.get();
            deleteCustomer.setIsActive(false);
            customerDetailsRepository.save(deleteCustomer);
            log.info("Customer deleted successfully with ID: {}", id);
            return "Customer Deleted successfully: " + deleteCustomer.getCustomerName();
        } else {
            log.error("Failed to delete customer: No existing customer found with ID: {}", id);
            return "Failed to delete customer";
        }
    }

    private CustomerDetailsDto convertToDto(CustomerDetailsEntity customerDetailsEntity) {
        CustomerDetailsDto dto = new CustomerDetailsDto();
        BeanUtils.copyProperties(customerDetailsEntity, dto);
        return dto;
    }

    @Override
    public List<String> getAllCustomerNames() {
        log.info("Fetching all customer names from the database.");
        return customerDetailsRepository.findAllCustomerNames();
    }


    public List<RegionDTO> getAllStates(String countryCode) {
        String url = "https://countriesnow.space/api/v0.1/countries/states/q?country="+countryCode;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, ResponseDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<RegionDTO> regionDTOList = response.getBody().getData().getStates();
                return regionDTOList;
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {

            }

        return Collections.emptyList();
    }

    public List<String> getAllCities(String countryCode, String stateCode) {
//        https://countriesnow.space/api/v0.1/countries/state/cities/q?country=India&state=Karnataka
        String url = "https://countriesnow.space/api/v0.1/countries/cities/q?country=" + countryCode + "&state=" + stateCode;
        log.info("Country code: " + countryCode + ", State code: " + stateCode);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CityResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, CityResponseDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<String> cityList = response.getBody().getData();
            return cityList;
        } else {
            log.error("Failed to retrieve cities. HTTP Status: {}", response.getStatusCode());
        }

        return Collections.emptyList();
    }

    @Override
    public List<CustomerDetailsDto> suggestionByName(String customerName) {
        List<CustomerDetailsEntity> listOfEntities = customerDetailsRepository.suggestionByName(customerName);
        return listOfEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDetailsDto findByEmail(String customerEmailId) {
        return  convertToDto(customerDetailsRepository.findByEmail(customerEmailId));
    }

    @Override
    public CustomerDetailsDto findByCustomerName(String customerName) {
        return  convertToDto(customerDetailsRepository.findByCustomerName(customerName));
    }

    @Override
    public String checkByCustomerName(String customerName) {
        if(customerName!=null && customerDetailsRepository.checkCustomerName(customerName)){
            log.info("custome name is:{}",customerName);
                 return "Name Already Exists";
            }
        return "";
    }
}
