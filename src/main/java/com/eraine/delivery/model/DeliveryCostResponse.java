package com.eraine.delivery.model;

import java.math.BigDecimal;
import java.util.Currency;

public record DeliveryCostResponse(
        String classification,
        Currency currency,
        BigDecimal cost) {
}
