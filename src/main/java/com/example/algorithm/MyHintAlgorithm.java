package com.example.algorithm;

import java.util.Collection;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

public class MyHintAlgorithm implements HintShardingAlgorithm<String> {



  @Override
  public void init() {
    System.out.println("初始化"+this.hashCode());
  }

  @Override
  public String getType() {
    return "HINT_FORCE";
  }

  /**
   *
   * @param collection 数据源名称集合
   * @param hintShardingValue 传入的判断依据
   * @return 要查询的数据源集合，如果返回多个数据源，会把多个数据源联合起来查询
   */
  @Override
  public Collection<String> doSharding(Collection<String> collection,
      HintShardingValue<String> hintShardingValue) {
    System.out.println("获取到hintShardingValue： " + hintShardingValue);
    System.out.println("获取到collection： " + collection);
    return collection;
  }
}
