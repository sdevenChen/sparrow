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



import com.sdeven.sparrow.starter.nodeflow.groovy.ExprSupport;

import java.util.ArrayList;
import java.util.Map;

/**
  * @Description Groovy 规则组件
  * @Author sdeven
  * @Create 12/12/20 10:18
  */
public class GroovyRuleSetComponent implements NfComponent {
   private static final String KEY_RULESET = "ruleset";

   @Override
   public void execute(Map<String, Object> node, Map<String, Object> ctx) {
       ArrayList<String> ruleset = (ArrayList<String>)node.get(KEY_RULESET);
       StringBuffer buf = new StringBuffer();
       for(int i=0;i<ruleset.size();i++) {
           if(i!=0) {
               buf.append(";");
           }
           buf.append(ruleset.get(i));
       }
       ExprSupport.parseExpr(buf.toString(), ctx);
   }

   @Override
   public String spiId() {
       return KEY_RULESET;
   }

}
