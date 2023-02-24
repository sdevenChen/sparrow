package com.sdeven.sparrow.starter.nodeflow.spi;

import java.util.Map;

/**
  * @Description 延时组件
  * @Author sdeven.chen.dongwei@gmail.com
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
