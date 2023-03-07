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
package com.sdeven.sparrow.starter.nodeflow.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.Hashtable;
import java.util.Map;

/**
  * Custom Expression Call Executor
  * @author sdeven
  *
  */
public class ExprSupport {
   private static final Object LOCK = new Object();
   private static final GroovyShell SHELL;

   private static Hashtable<String, Script> cache = new Hashtable<>();
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
