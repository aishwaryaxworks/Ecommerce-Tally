package com.netzwerk.ecommerce.service.payment;

import com.netzwerk.ecommerce.dto.payment.PaymentDto;

public interface PaymentService {

    String savePayment(PaymentDto paymentDto);

}
