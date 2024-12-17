package com.eraine.delivery.service;

import com.eraine.delivery.model.DeliveryRuleParameter;
import com.eraine.delivery.model.ParcelRejectedException;
import com.eraine.delivery.persistence.RuleEntity;
import com.eraine.delivery.persistence.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RuleService ruleService;

    private List<RuleEntity> createRules() {
        return List.of(
                createRule(1, "Reject", "weight > 50", null),
                createRule(2, "Heavy Parcel", "weight > 10", "20 * weight"),
                createRule(3, "Small Parcel", "volume < 1500", "0.03 * volume"),
                createRule(4, "Medium Parcel", "volume < 2500", "0.04 * volume"),
                createRule(5, "Large Parcel", null, "0.05 * volume")
        );
    }

    private RuleEntity createRule(Integer priority, String name, String condition, String costFormula) {
        return RuleEntity.builder()
                .priority(priority)
                .name(name)
                .condition(condition)
                .costFormula(costFormula)
                .build();
    }

    @Test
    void whenMatchingFirstRuleFound_thenShouldEvaluate() {
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(new BigDecimal("10"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .build();

        when(ruleRepository.queryAll()).thenReturn(createRules());

        var result = ruleService.evaluate(ruleParameter);

        assertEquals("Small Parcel", result.classification());
        assertEquals(new BigDecimal("30.00"), result.cost());
    }

    @Test
    void whenCostFormulaIsNull_thenShouldThrowParcelRejectedException() {
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(new BigDecimal("51"))
                .height(new BigDecimal("10"))
                .width(new BigDecimal("10"))
                .length(new BigDecimal("10"))
                .build();

        when(ruleRepository.queryAll()).thenReturn(createRules());

        assertThrows(ParcelRejectedException.class,
                () -> ruleService.evaluate(ruleParameter));
    }

    @Test
    void whenConditionIsNull_thenShouldEvaluateLowestPriorityRule() {
        var ruleParameter = DeliveryRuleParameter.builder()
                .weight(new BigDecimal("9"))
                .height(new BigDecimal("40"))
                .width(new BigDecimal("40"))
                .length(new BigDecimal("40"))
                .build();

        when(ruleRepository.queryAll()).thenReturn(createRules());

        var result = ruleService.evaluate(ruleParameter);

        assertEquals("Large Parcel", result.classification());
        assertEquals(new BigDecimal("3200.00"), result.cost());
    }
}
