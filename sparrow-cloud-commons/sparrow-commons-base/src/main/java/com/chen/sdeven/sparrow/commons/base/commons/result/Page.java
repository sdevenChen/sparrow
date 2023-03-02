/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
