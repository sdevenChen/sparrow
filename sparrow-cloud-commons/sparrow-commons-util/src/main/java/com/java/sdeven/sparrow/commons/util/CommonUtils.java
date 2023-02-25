package com.java.sdeven.sparrow.commons.util;

import lombok.extern.slf4j.Slf4j;


/**
 * 公共工具类
 *
 * @author sdeven
 * @since 2020/3/18
 */
@Slf4j
public class CommonUtils {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 将大小格式化为 2的N次
     *
     * @param cap 初始大小
     * @return 格式化后的大小，2的N次
     */
    public static int formatSize(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

}
