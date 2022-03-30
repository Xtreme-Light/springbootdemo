package com.example.algorithm;

import java.util.Collection;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.springframework.stereotype.Service;

@Service
public class SpringBootBaseDataSourceAlgorithmImpl implements ISpringBootBaseDataSourceAlgorithm{

  @Override
  public Collection<String> determineDataSource(Collection<String> collection,
      HintShardingValue<String> hintShardingValue) {
    System.out.println("开始计算~~~");
    return collection;
  }
}
