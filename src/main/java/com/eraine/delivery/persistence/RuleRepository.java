package com.eraine.delivery.persistence;

import java.util.List;

public interface RuleRepository {

    List<RuleEntity> queryAll();
}
