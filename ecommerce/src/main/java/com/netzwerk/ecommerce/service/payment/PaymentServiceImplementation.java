package com.netzwerk.ecommerce.service.payment;

import com.netzwerk.ecommerce.dto.payment.PaymentDto;
import com.netzwerk.ecommerce.entity.CustomerDetailsEntity;
import com.netzwerk.ecommerce.entity.OrderEntity;
import com.netzwerk.ecommerce.entity.PaymentEntity;
import com.netzwerk.ecommerce.repository.CustomerDetailsRepository;
import com.netzwerk.ecommerce.repository.OrderRepository;
import com.netzwerk.ecommerce.repository.payment.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PaymentServiceImplementation implements PaymentService{
    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;
    private CustomerDetailsRepository customerDetailsRepository;

    @Override
    public String savePayment(PaymentDto paymentDto) {
        paymentRepository.save(convertToEntity(paymentDto));
        return "Saved successfully";
    }

    private PaymentEntity convertToEntity(PaymentDto paymentDto) {
        return PaymentEntity.builder()
                .order(getOrderData(paymentDto))
                .paymentMode(paymentDto.getPaymentMode())
                .paidAmount(paymentDto.getPaidAmount())
                .remainingBalance(paymentDto.getRemainingBalance())
                .nextPaymentDate(paymentDto.getNextPaymentDate())
                .createdBy(paymentDto.getCreatedBy())
                .createdOn(LocalDate.now())
                .build();
    }

    public OrderEntity getOrderData(PaymentDto paymentDto) {
        OrderEntity order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderDueDate(paymentDto.getNextPaymentDate());
        customerDetailsRepository.findById(order.getCustomerDetailsEntity().getCustomerId())
                .ifPresent(customer -> {
                    Double currentPayable = customer.getCustomerPayableAmount();
                    Double paidAmount = paymentDto.getPaidAmount();
                    customer.setCustomerPayableAmount(
                            (currentPayable != null ? currentPayable : 0) - (paidAmount != null ? paidAmount : 0)
                    );
                    customerDetailsRepository.save(customer);
                });

        return orderRepository.save(order);
    }



}
