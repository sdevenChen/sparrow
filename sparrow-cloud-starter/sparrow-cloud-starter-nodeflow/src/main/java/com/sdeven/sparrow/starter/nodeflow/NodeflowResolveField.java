package com.sdeven.sparrow.starter.nodeflow;

/**
 * @Description 节点规则引擎解析支持字段
 * @Author sdeven.chen.dongwei@gmail.com
 */
public class NodeflowResolveField {
    /**
     * 定义层，一级节点字段，indexs 负责定义输入输出、类型、校验正则
     */
    public static final String KEY_INDEXS = "indexs";
    public static final String KEY_NODES = "nodes";

    /**
     * 执行层，二级节点执行
     */
    public static final String KEY_ID = "id";
    public static final String KEY_NEXT = "next";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_COMPONENT = "component";
    public static final String KEY_ERROR = "error";
    public static final String KEY_OPT = "opt";
    public static final String KEY_TYPE = "type";
}
