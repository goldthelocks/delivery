package com.eraine.delivery.persistence;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Repository
public class DdbRuleRepository implements RuleRepository {

    private final DynamoDbTable<RuleEntity> ruleTable;

    public DdbRuleRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.ruleTable = dynamoDbEnhancedClient.table(RuleEntity.TABLE_NAME,
                TableSchema.fromBean(RuleEntity.class));
    }

    public List<RuleEntity> queryAll() {
        return queryToList(ruleTable.scan())
                .stream()
                .sorted(Comparator.comparingInt(RuleEntity::getPriority))
                .toList();
    }

    private List<RuleEntity> queryToList(PageIterable<RuleEntity> rules) {
        return StreamSupport.stream(rules.spliterator(), false)
                .flatMap(page -> page.items().stream())
                .toList();
    }
}
