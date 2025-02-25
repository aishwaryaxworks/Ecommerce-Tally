package com.netzwerk.ecommerce.repository;

import com.netzwerk.ecommerce.entity.CustomerDetailsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetailsEntity, Integer> {
    @Query("SELECT COUNT(c) > 0 FROM CustomerDetailsEntity c WHERE c.customerEmailId = :customerEmailId AND c.isActive = true")
    boolean checkCustomerEmail(@Param("customerEmailId") String customerEmailId);
    @Query("SELECT COUNT(c) > 0 FROM CustomerDetailsEntity c WHERE c.contactNumber = :contactNumber AND c.isActive = true")
    boolean checkCustomerContactNumber(@Param("contactNumber") String contactNumber);
    @Query("SELECT COUNT(c) > 0 FROM CustomerDetailsEntity c WHERE c.gstNumber = :gstNumber AND c.isActive = true")
    boolean checkCustomerGstNumber(@Param("gstNumber") String gstNumber);
    @Query("SELECT c.customerName FROM CustomerDetailsEntity c WHERE c.isActive = true")
    List<String> findAllCustomerNames();

    @Query("SELECT c.customerName FROM CustomerDetailsEntity c WHERE c.isActive = true")
    List<String> getCustomerName();

    CustomerDetailsEntity getById(Integer id);

    Page<CustomerDetailsEntity> findByIsActiveTrueOrderByCustomerIdDesc(Pageable pageable);

    @Query("SELECT c FROM CustomerDetailsEntity c WHERE c.customerName LIKE %:customerName% AND c.isActive = true")
    List<CustomerDetailsEntity> suggestionByName(@Param("customerName") String customerName);

    @Query("SELECT c FROM CustomerDetailsEntity c WHERE c.customerEmailId = :customerEmailId AND c.isActive = true")
    CustomerDetailsEntity findByEmail(String customerEmailId);

    @Query("SELECT c FROM CustomerDetailsEntity c WHERE c.customerName = :customerName AND c.isActive = true")
    CustomerDetailsEntity findByCustomerName(String customerName);

    @Query("SELECT COUNT(c) > 0 FROM CustomerDetailsEntity c WHERE c.customerName = :customerName AND c.isActive = true")
    boolean checkCustomerName(@Param("customerName") String customerName);


}

