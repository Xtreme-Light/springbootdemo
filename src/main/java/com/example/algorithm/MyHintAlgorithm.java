package com.example.algorithm;

import java.util.Collection;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyHintAlgorithm implements HintShardingAlgorithm<String> , ApplicationContextAware {

  private ApplicationContext applicationContext;

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
    final ISpringBootBaseDataSourceAlgorithm bean = applicationContext.getBean(
        ISpringBootBaseDataSourceAlgorithm.class);
    return bean.determineDataSource(collection,hintShardingValue);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
