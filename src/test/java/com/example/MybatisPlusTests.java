package com.example;

import com.example.holder.HintContextHolder;
import com.example.mapper.StandaloneMapper;
import com.example.mapper.UserMapper;
import com.example.model.StandaloneDO;
import com.example.model.UserDO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisPlusTests {

  /**
   * 配置了规则的表
   */
  @Autowired
  UserMapper userMapper;
  /**
   * 没有配置规则的缺省表
   */
  @Autowired
  StandaloneMapper standaloneMapper;
  /**
   * 默认情况下，使用了ds0
   */
  @Test
  void origin() {
    final List<StandaloneDO> standaloneDOS = standaloneMapper.selectList(null);
    standaloneDOS.forEach(System.out::println);
  }
  @Test
  void dynamic4RuleTable() {
    final HintContextHolder hintContextHolder = new HintContextHolder();
    hintContextHolder.pushDatabaseShardingValue("ds1");
    List<UserDO> userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
    assert userList.size() == 5;
    hintContextHolder.poll();
    hintContextHolder.pushDatabaseShardingValue("ds0");
    userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
    assert userList.size() == 2;
    hintContextHolder.poll();
    userList = userMapper.selectList(null);
    assert userList.size() == 5;
    userList.forEach(System.out::println);
  }
  @Test
  void dynamic4StandaloneTable() {
    final HintContextHolder hintDataSourceContextHolder = new HintContextHolder();
    hintDataSourceContextHolder.pushDataSourceName("ds1");
    List<StandaloneDO> standaloneDOS = standaloneMapper.selectList(null);
    standaloneDOS.forEach(System.out::println);
    assert standaloneDOS.size() == 2;
    hintDataSourceContextHolder.poll();
    hintDataSourceContextHolder.pushDataSourceName("ds0");
    standaloneDOS = standaloneMapper.selectList(null);
    standaloneDOS.forEach(System.out::println);
    assert standaloneDOS.size() == 1;
    hintDataSourceContextHolder.poll();
    standaloneDOS = standaloneMapper.selectList(null);
    // 因为指定数据库名称用完了，默认的就是第一个数据源
    assert standaloneDOS.size() == 1;
    standaloneDOS.forEach(System.out::println);
    hintDataSourceContextHolder.clear();
  }
  @Test
  void dynamic4MixTable() {
    final HintContextHolder hintContextHolder = new HintContextHolder();

    hintContextHolder.pushDatabaseShardingValue("ds1");
    List<UserDO> userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
    hintContextHolder.poll();


    hintContextHolder.pushDataSourceName("ds0");
    userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
    hintContextHolder.poll();

    userList = userMapper.selectList(null);
    userList.forEach(System.out::println);


    hintContextHolder.pushDataSourceName("ds0");
    userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
    hintContextHolder.poll();


    hintContextHolder.pushDatabaseShardingValue("ds1");
    userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
    hintContextHolder.clear();

  }
}
