package com.eraine.delivery.service;

import com.eraine.delivery.model.DeliveryRuleEvaluationResult;
import com.eraine.delivery.model.DeliveryRuleParameter;
import com.eraine.delivery.model.ParcelRejectedException;
import com.eraine.delivery.persistence.RuleEntity;
import com.eraine.delivery.persistence.RuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class RuleService {

    private final ExpressionParser parser;
    private final RuleRepository ruleRepository;

    public RuleService(@Qualifier("ddbRuleRepository") RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
        this.parser = new SpelExpressionParser();
    }

    public DeliveryRuleEvaluationResult evaluate(DeliveryRuleParameter deliveryRuleParameter) {
        final var evaluationContext = new StandardEvaluationContext(deliveryRuleParameter);

        return ruleRepository.queryAll().stream()
                .filter(rule -> evaluateCondition(rule, evaluationContext))
                .findFirst()
                .map(rule -> applyRule(rule, evaluationContext))
                .orElseThrow(() -> new RuntimeException("No matching rule found."));
    }

    private boolean evaluateCondition(RuleEntity rule, StandardEvaluationContext evaluationContext) {
        return rule.getCondition() == null || Boolean.TRUE.equals(parser.parseExpression(rule.getCondition()).getValue(evaluationContext, Boolean.class));
    }

    private DeliveryRuleEvaluationResult applyRule(RuleEntity rule, StandardEvaluationContext evaluationContext) {
        if (rule.getCostFormula() == null) {
            throw new ParcelRejectedException("Weight limit exceeded.");
        }

        log.info("Applying rule: {}", rule);
        var cost = parser.parseExpression(rule.getCostFormula()).getValue(evaluationContext, BigDecimal.class);
        return new DeliveryRuleEvaluationResult(rule.getName(), cost);
    }
}
