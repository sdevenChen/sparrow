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
package com.sdeven.sparrow.starter.nodeflow.spi;

import java.util.Map;

/**
  * @Description 延时组件
  * @Author sdeven
  * @Create 12/12/20 10:20
  */
public class WaitingComponent implements NfComponent {

   @Override
   public void execute(Map<String, Object> node, Map<String, Object> ctx) {
       Object waiting = node.get("waiting");
       long timeout = 2000;
       if(waiting!=null) {
           timeout = ((int) waiting) * 1000;
       }
       try {
           Thread.sleep(timeout);
       } catch (Exception e) {
           long startTime = System.currentTimeMillis();
           while((System.currentTimeMillis()-startTime)<timeout) {
           }
       }
   }

   @Override
   public String spiId() {
       return "waiting_cmp";
   }
}
