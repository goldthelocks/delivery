package com.eraine.delivery.persistence;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MockRuleRepository implements RuleRepository {

    @Override
    public List<RuleEntity> queryAll() {
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
}
