package com.sdeven.sparrow.starter.nodeflow.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.Hashtable;
import java.util.Map;

/**
  * @Description 自定义表达式调用
  * @Author sdeven.chen.dongwei@gmail.com
  * @Create 12/12/20 10:18
  *
  */
public class ExprSupport {
   private static final Object LOCK = new Object();
   private static final GroovyShell SHELL;

   private static Hashtable<String, Script> cache = new Hashtable<String, Script>();
   static {
       CompilerConfiguration cfg = new CompilerConfiguration();
       cfg.setScriptBaseClass(MyBasicScript.class.getName());

       SHELL = new GroovyShell(cfg);
   }

   public static Object parseExpr(String expr) {
       Script s = getScriptFromCache(expr);
       return s.run();
   }

   public static Object parseExpr(String expr, Map<?, ?> map) {
       Binding binding = new Binding(map);
       Script script = getScriptFromCache(expr);
       script.setBinding(binding);
       return script.run();
   }

   private static Script getScriptFromCache(String expr) {
       if (cache.contains(expr)) {
           return cache.get(expr);
       }
       synchronized (LOCK) {
           if (cache.contains(expr)) {
               return cache.get(expr);
           }
           Script script = SHELL.parse(expr);
           cache.put(expr, script);
           return script;
       }
   }
}
