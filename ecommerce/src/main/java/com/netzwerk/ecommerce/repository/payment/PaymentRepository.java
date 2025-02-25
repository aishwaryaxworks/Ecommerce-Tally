package com.netzwerk.ecommerce.repository.payment;

import com.netzwerk.ecommerce.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
}
