package com.java.sdeven.sparrow.commons.idgenerator;

/**
 * IDGenerator
 *
 * @Date 2020/2/17 下午6:13
 * @Author  sdeven.chen.dongwei@gmail.com
 */
public interface IdGenerator {
    long nextId(String name);
    long nextId();
}
