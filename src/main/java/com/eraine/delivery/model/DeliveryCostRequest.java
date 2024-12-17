package com.eraine.delivery.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DeliveryCostRequest(
        BigDecimal weight,
        BigDecimal height,
        BigDecimal width,
        BigDecimal length,
        String voucherCode
) {
}
