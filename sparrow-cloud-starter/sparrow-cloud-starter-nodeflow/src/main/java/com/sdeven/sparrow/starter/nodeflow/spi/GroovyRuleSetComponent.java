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
