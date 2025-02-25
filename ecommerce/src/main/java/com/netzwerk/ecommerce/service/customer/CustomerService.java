package com.netzwerk.ecommerce.service.customer;

import com.netzwerk.ecommerce.dto.CustomerDetailsDto;
import com.netzwerk.ecommerce.dto.RegionDTO;
import com.netzwerk.ecommerce.entity.CustomerDetailsEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    String onSave(CustomerDetailsDto customerDetailsDto);

    String checkCustomerEmail(String customerEmail);

    String checkCustomerContactNumber(String contactNumber);

    String checkCustomerGstNumber(String gstNumber);

    Page<CustomerDetailsDto> findPaginated(int pageNo, int pageSize);

    CustomerDetailsDto getCustomerDetails(int id);

    String updateCustomer(CustomerDetailsDto customerDetailsDto);

    String deleteCustomer(int id);
    
    List<String> getAllCustomerNames();

    List<RegionDTO>  getAllStates(String code);

    List<String> getAllCities(String countryCode,String stateCode);
    List<CustomerDetailsDto> suggestionByName(String customerName);

    CustomerDetailsDto findByEmail(String customerEmailId);
    CustomerDetailsDto findByCustomerName(String customerName);
    String checkByCustomerName(String customerName);

}
