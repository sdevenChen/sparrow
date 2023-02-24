package com.java.sdeven.sparrow.commons.idgenerator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * ZookeeperProperties
 *
 * @Date 2020/2/17 下午4:37
 * @Author  sdeven.chen.dongwei@gmail.com
 */
@Data
@ConfigurationProperties("sdeven.commons.idgenerator")
public class IdGeneratorProperties {

    private Boolean enabled;

    private Integer dataCenterId;
    private String zkAppPath;

    @NestedConfigurationProperty
    private CuratorProperties curator;
}
