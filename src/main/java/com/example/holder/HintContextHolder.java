package com.example.holder;

import groovy.lang.Tuple2;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.core.NamedThreadLocal;

public final class HintContextHolder {


  private final HintManager hintManager;

  private final ThreadLocal<Deque<Tuple2<Integer, String>>> SHARDING_VALUE_HOLDER = new NamedThreadLocal<>(
      "switch-datasource") {
    @Override
    protected Deque<Tuple2<Integer, String>> initialValue() {
      return new ArrayDeque<>();
    }
  };

  public HintContextHolder() {
    this.hintManager = HintManager.getInstance();
  }

  public Tuple2<Integer, String> peek() {
    return SHARDING_VALUE_HOLDER.get().peek();
  }

  public String pushDatabaseShardingValue(String shardingValue) {
    String shardingValueDataBase = Optional.ofNullable(shardingValue).orElse("");
    final Tuple2<Integer, String> tuple2 = new Tuple2<>(2,shardingValue);
    final Deque<Tuple2<Integer, String>> tuple2s = SHARDING_VALUE_HOLDER.get();
    tuple2s.push(tuple2);
    hintManager.setDatabaseShardingValue(shardingValueDataBase);
    final List<Tuple2<Integer, String>> collect = tuple2s.stream().filter(v -> v.getV1() == 1)
        .collect(Collectors.toList());
    if (collect.size()>1) {
      throw new RuntimeException("数据异常");
    }
    if (collect.size() == 1) {
      final Tuple2<Integer, String> integerStringTuple2 = collect.get(0);
      final String v2 = integerStringTuple2.getV2();
      final String s = HintManager.getDataSourceName().orElse(null);
      if (v2.equals(s)) {
        hintManager.setDataSourceName(null);
      }else {
        throw new RuntimeException();
      }
    }
    return shardingValueDataBase;
  }

  public String pushDataSourceName(String dataSourceName) {
    String shardingValueDataBase = Optional.ofNullable(dataSourceName).orElse("");
    final Tuple2<Integer, String> tuple2 = new Tuple2<>(1,shardingValueDataBase);
    SHARDING_VALUE_HOLDER.get().push(tuple2);
    hintManager.setDataSourceName(shardingValueDataBase);
    return shardingValueDataBase;
  }

  public void poll() {
    Deque<Tuple2<Integer, String>> deque = SHARDING_VALUE_HOLDER.get();
    final Tuple2<Integer, String> poll = deque.poll();
    final Tuple2<Integer, String> peek = peek();
    if (poll != null) {
      // 弹出的是数据源，则清除数据源
      if (1 == poll.getV1()) {
        hintManager.setDataSourceName(null);
      } else {
        // 弹出的是 hintValue，那么用当前栈顶的元素
        if (peek != null) {
          if (peek.getV1() == 1) {
            hintManager.setDataSourceName(peek.getV2());
          }else {
            hintManager.setDatabaseShardingValue(peek.getV2());
          }
        }
      }
    }

    if (deque.isEmpty()) {
      SHARDING_VALUE_HOLDER.remove();
    }
  }

  public HintManager getHintManager() {
    return hintManager;
  }

  public void clear() {
    hintManager.close();
    SHARDING_VALUE_HOLDER.remove();
  }
}
