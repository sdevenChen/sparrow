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
  * Paging wrapped generic objects
  * @author sdeven
  */
@Data
public class Page<T> implements Serializable{

   private static final long serialVersionUID = -5056954732663923333L;

   /**
    * Query result List
    */
   public List<T> records = Collections.emptyList();

   /**
    * Total number of records
    */
   public long total = 0;
   /**
    * Number of bars per page, default 10
    */
   public long size = 10;

   /**
    * Current page
    */
   public long current = 1;

   /**
    * Total number of pages
    */
   private long pages;

}
