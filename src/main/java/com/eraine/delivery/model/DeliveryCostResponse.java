package com.eraine.delivery.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
public record DeliveryCostResponse(
        String classification,
        Currency currency,
        BigDecimal cost) {
}
