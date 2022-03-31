package com.example.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 这个表 是没有配置的缺省表
 */
@Data
@TableName("t_stand_alone")
public class StandaloneDO {

  private Integer id;
  private String standalone;
}
