package com.eraine.delivery.service;

import com.eraine.delivery.client.VoucherClient;
import com.eraine.delivery.model.DeliveryCostRequest;
import com.eraine.delivery.model.DeliveryCostResponse;
import com.eraine.delivery.model.DeliveryRuleParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@RequiredArgsConstructor
@Slf4j
@Service
public class DeliveryCostService {

    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("PHP");
    private static final BigDecimal HUNDRED_PERCENT = new BigDecimal("100");

    private final RuleService ruleService;
    private final VoucherClient voucherRestClient;

    public DeliveryCostResponse calculateCost(DeliveryCostRequest request) {
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(request.weight())
                .height(request.height())
                .width(request.width())
                .length(request.length())
                .build();

        log.info("Calculating delivery cost with these parameters: {}", ruleParameter);
        var evaluationResult = ruleService.evaluate(ruleParameter);
        var costWithDiscount = request.voucherCode() == null
                ? evaluationResult.cost()
                : applyDiscount(evaluationResult.cost(), request.voucherCode());

        return new DeliveryCostResponse(
                evaluationResult.classification(),
                DEFAULT_CURRENCY,
                costWithDiscount);
    }

    private BigDecimal applyDiscount(BigDecimal calculatedCost, String voucherCode) {
        try {
            // assuming this is percentage (e.g. 10 for 10%)
            var discount = voucherRestClient.getDiscount(voucherCode);
            log.info("Applying {}% discount from voucher to calculatedCost {}", discount, calculatedCost);
            var discountAmount = calculatedCost.multiply(discount).divide(HUNDRED_PERCENT, RoundingMode.HALF_UP);
            return calculatedCost.subtract(discountAmount);
        } catch (RestClientException ex) {
            log.error("Unable to apply discount due to API error - returning original calculated cost", ex);
            return calculatedCost;
        }
    }
}
