package com.example.algorithm;

import java.util.Collection;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

public interface ISpringBootBaseDataSourceAlgorithm {

  Collection<String> determineDataSource(Collection<String> collection,HintShardingValue<String> hintShardingValue);

}
