package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootShardingSphereDemoApplicationTests {

  @Autowired
  private DataSource dataSource;

  /**
   * 没有设置数据源时，查询了所有数据源中的 t_user 表
   * 既查了1库数据，又查了2库数据
   *
   * @throws SQLException sql异常
   */
  @Test
  void contextLoads() throws SQLException {
    try (
        final Connection connection = dataSource.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "select id,name from t_user");
    ) {
      final ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        final String string = resultSet.getString(2);
        System.out.println(string);
      }
    }
  }

  /**
   * 仅查询2库数据（切换数据源）
   */
  @Test
  void test2() {
    try (
        final HintManager instance = HintManager.getInstance();
    ) {
      instance.setDataSourceName("ds1");
      try (
          final Connection connection = dataSource.getConnection();
          final PreparedStatement preparedStatement = connection.prepareStatement(
              "select id,name from t_user");
      ) {
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          final String string = resultSet.getString(2);
          System.out.println(string);
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }
  /**
   * 查询2库数据后查询1库（切换数据源）
   */
  @Test
  void test3() {
    try (
        final HintManager instance = HintManager.getInstance();
    ) {
      instance.setDataSourceName("ds1");
      try (
          final Connection connection = dataSource.getConnection();
          final PreparedStatement preparedStatement = connection.prepareStatement(
              "select id,name from t_user");
      ) {
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          final String string = resultSet.getString(2);
          System.out.println(string);
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
      instance.clearShardingValues();
      System.out.println("中间停顿");
      instance.setDataSourceName("ds0");
      try (
          final Connection connection = dataSource.getConnection();
          final PreparedStatement preparedStatement = connection.prepareStatement(
              "select id,name from t_user");
      ) {
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          final String string = resultSet.getString(2);
          System.out.println(string);
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }

  /**
   * 没有指定数据源，同时，没有配置表t_stand_alone，此时，默认使用配置的第一个数据源作为基础数据源。
   * @throws SQLException
   */
  @Test
  void test4() throws SQLException {
    try (
        final Connection connection = dataSource.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "select id,standalone from t_stand_alone");
    ) {
      final ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        final String string = resultSet.getString(2);
        System.out.println(string);
      }
    }
  }

  /**
   * 仅查询2库数据 通过指定的算法规则计算
   */
  @Test
  void test2_2() {
    try (
        final HintManager instance = HintManager.getInstance();
    ) {
      instance.setDatabaseShardingValue("ds1");
      try (
          final Connection connection = dataSource.getConnection();
          final PreparedStatement preparedStatement = connection.prepareStatement(
              "select id,name from t_user");
      ) {
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          final String string = resultSet.getString(2);
          System.out.println(string);
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }
}
