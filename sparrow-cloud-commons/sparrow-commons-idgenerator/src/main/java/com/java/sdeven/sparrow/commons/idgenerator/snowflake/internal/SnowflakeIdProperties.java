package com.java.sdeven.sparrow.commons.idgenerator.snowflake.internal;

import lombok.Data;

/**
 * SnowflakeIdProperties
 *
 * @Date 2020/2/17 下午4:37
 * @Author  sdeven.chen.dongwei@gmail.com
 */
@Data
public class SnowflakeIdProperties {
    private Integer dataCenterId;
    private String zkIdAppPath;
}
