package com.eraine.delivery.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DeliveryCostResponse(
        String classification,
        String currency,
        BigDecimal cost) {
}
