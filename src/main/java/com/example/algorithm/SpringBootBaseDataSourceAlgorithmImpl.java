package com.example.algorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.springframework.stereotype.Service;

@Service
public class SpringBootBaseDataSourceAlgorithmImpl implements ISpringBootBaseDataSourceAlgorithm{

  @Override
  public Collection<String> determineDataSource(Collection<String> collection,
      HintShardingValue<String> hintShardingValue) {
    System.out.println("开始计算~~~" + collection + "   " + hintShardingValue);
    final List<String> collect = hintShardingValue.getValues().stream().filter(Objects::nonNull)
        .collect(Collectors.toList());
    if (collect.isEmpty()) {
      return Collections.singleton("ds1");
    }
    return hintShardingValue.getValues();
  }
}
