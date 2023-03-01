package com.chen.sdeven.sparrow.commons.base.commons.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Data;

/**
  * @Description Page 输出对象
  * @Author sdeven
  * @Create 11/25/20 10:37
  */
@Data
public class Page<T> implements Serializable{

   /**
    *
    */
   private static final long serialVersionUID = -5056954732663923333L;

   /**
    * 查询数据列表
    */
   public List<T> records = Collections.emptyList();

   /**
    * 总记录数
    */
   public long total = 0;
   /**
    * 每页显示条数，默认 10
    */
   public long size = 10;

   /**
    * 当前页
    */
   public long current = 1;

   // 总页数
   private long pages;

}
