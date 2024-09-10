package com.sdeven.sparrow.commons.abtest.handler;


import com.sdeven.sparrow.commons.abtest.constant.AbTestAlgorithmEnum;

/**
 * @author sdeven
 * @date 2024/6/14 9:39
 * @since 1.0.0
 */
public interface AlgorithmStrategyHandler {

    /**
     * 计算号段
     * @param cid 用户ID
     * @param salt 盐值
     */
    String calculateSegment(String cid, String salt);

    /**
     * 支持的算法策略
     *
     * @return AbExperimentGroupBucketAlgorithm
     */
        AbTestAlgorithmEnum supportType();
}
