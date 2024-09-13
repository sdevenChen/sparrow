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
package com.sdeven.sparrow.commons.abtest.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 实验组分桶算法枚举
 * </p>
 *
 * @author zhouwei
 * @since 2023/10/30 14:55
 */
@Getter
@ToString
@AllArgsConstructor
public enum AbTestAlgorithmEnum {

    /**
     * 十进制 算法 分100号段
     */
    DECIMAL_100,
    /**
     * 十进制 算法 分10号段
     */
    DECIMAL_10,
    /**
     * 十六进制 算法 分16号段
     */
    HEXADECIMAL_16;

    /**
     * 通过名称获取枚举对象
     *
     * @param name 枚举名称
     * @return 枚举对象
     */
    public static AbTestAlgorithmEnum getEnum(String name) {
        for (AbTestAlgorithmEnum algorithmEnum : values()) {
            if (algorithmEnum.name().equals(name)) {
                return algorithmEnum;
            }
        }
        return null;
    }

    /**
     * 获取所有实验组分桶算法名称列表
     *
     * @return 实验组分桶算法名称列表
     */
    public static List<String> getAllNameList() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
    }

}
