package com.eraine.delivery.model;

import java.math.BigDecimal;

public record DeliveryRuleEvaluationResult(
        String classification,
        BigDecimal cost) {
}
