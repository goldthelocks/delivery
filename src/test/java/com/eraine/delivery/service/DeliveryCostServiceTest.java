package com.eraine.delivery.service;

import com.eraine.delivery.client.VoucherClient;
import com.eraine.delivery.model.DeliveryCostRequest;
import com.eraine.delivery.model.DeliveryRuleEvaluationResult;
import com.eraine.delivery.model.DeliveryRuleParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryCostServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private VoucherClient voucherClient;

    @InjectMocks
    private DeliveryCostService deliveryCostService;

    @Test
    void shouldCalculateCostWithoutVoucherCode() {
        var deliveryCostRequest = DeliveryCostRequest.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .build();
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .build();
        var evaluationResult = new DeliveryRuleEvaluationResult(
                "Small Parcel",
                new BigDecimal("30.00"));
        when(ruleService.evaluate(ruleParameter)).thenReturn(evaluationResult);

        var deliveryCostResponse = deliveryCostService.calculateCost(deliveryCostRequest);

        assertEquals("Small Parcel", deliveryCostResponse.classification());
        assertEquals("PHP", deliveryCostResponse.currency());
        assertEquals(new BigDecimal("30.00"), deliveryCostResponse.cost());
    }

    @Test
    void shouldCalculateCostWithVoucherCode() {
        var deliveryCostRequest = DeliveryCostRequest.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .voucherCode("MYNT")
                .build();
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .build();
        var evaluationResult = new DeliveryRuleEvaluationResult(
                "Small Parcel",
                new BigDecimal("30.00"));
        when(ruleService.evaluate(ruleParameter)).thenReturn(evaluationResult);
        when(voucherClient.getDiscount("MYNT")).thenReturn(new BigDecimal("10"));

        var deliveryCostResponse = deliveryCostService.calculateCost(deliveryCostRequest);

        assertEquals("Small Parcel", deliveryCostResponse.classification());
        assertEquals("PHP", deliveryCostResponse.currency());
        assertEquals(new BigDecimal("27.00"), deliveryCostResponse.cost());
    }

    @Test
    void shouldCalculateCostWithoutDiscount_whenVoucherApiThrowsRestClientException() {
        var deliveryCostRequest = DeliveryCostRequest.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .voucherCode("MYNT")
                .build();
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .build();
        var evaluationResult = new DeliveryRuleEvaluationResult(
                "Small Parcel",
                new BigDecimal("30.00"));
        when(ruleService.evaluate(ruleParameter)).thenReturn(evaluationResult);
        when(voucherClient.getDiscount("MYNT")).thenThrow(new RestClientException("Something broke"));

        var deliveryCostResponse = deliveryCostService.calculateCost(deliveryCostRequest);

        assertEquals("Small Parcel", deliveryCostResponse.classification());
        assertEquals("PHP", deliveryCostResponse.currency());
        assertEquals(new BigDecimal("30.00"), deliveryCostResponse.cost());
    }
}
