package com.netzwerk.ecommerce.util;

import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class EcommerceUtil {

    private EcommerceUtil(){

    }

    // order number : NW/25-26/order id
    public static String generateOrderID(Integer orderId) {
        int currentYear = Year.now().getValue();
        int nextYear = currentYear + 1;
        String financialYear = (currentYear % 100) + "-" + (nextYear % 100);
        return "NW/" + financialYear + "/" + orderId;
    }
}
