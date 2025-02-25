package com.netzwerk.ecommerce.repository;


import com.netzwerk.ecommerce.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {

    Page<OrderEntity> findByIsActiveTrueOrderByIdDesc(Pageable pageable);

    OrderEntity getById(Integer id);

    @Query("SELECT order FROM OrderEntity order WHERE order.customerName LIKE %:customerName%")
    List<OrderEntity> suggestionByCustomerName(@Param("customerName") String customerName);

    @Query("SELECT order FROM OrderEntity order WHERE order.make = :make AND order.model = :model AND order.productCode = :productCode")
    List<OrderEntity> findByMakeModuleAndProductCode(@Param("make") String make, @Param("model") String model, @Param("productCode") String productCode);

    @Query("SELECT order FROM OrderEntity order WHERE order.voucherTypeName=:voucherTypeName")
    List<OrderEntity> findByVoucherType(@Param("voucherTypeName") String voucherTypeName);

    @Query("SELECT o FROM OrderEntity o WHERE o.voucherTypeName = :voucherTypeName AND o.isActive=True")
    Page<OrderEntity> findByVoucherType(@Param("voucherTypeName") String voucherTypeName, Pageable pageable);



}
