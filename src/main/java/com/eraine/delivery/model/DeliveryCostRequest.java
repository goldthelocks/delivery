package com.eraine.delivery.model;

import java.math.BigDecimal;

public record DeliveryCostRequest(
        BigDecimal weight,
        BigDecimal height,
        BigDecimal width,
        BigDecimal length,
        String voucherCode
) {
}
