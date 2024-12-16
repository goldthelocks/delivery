package com.eraine.delivery.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DeliveryRuleParameter(
        BigDecimal weight,
        BigDecimal height,
        BigDecimal width,
        BigDecimal length,
        BigDecimal volume
) {
    public DeliveryRuleParameter {
        if (height != null && width != null && length != null) {
            volume = height.multiply(width).multiply(length);
        }
    }
}
