package com.sdeven.sparrow.starter.nodeflow.spi;

import java.util.Map;


/**
  * @Description 组件父类
  * @Author sdeven.chen.dongwei@gmail.com
  * @Create 12/12/20 10:18
  */
public interface NfComponent {
    void execute(Map<String, Object> node, Map<String, Object> ctx);
    String spiId();
}
