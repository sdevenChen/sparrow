package com.sdeven.sparrow.commons.abtest.handler;

import com.sdeven.sparrow.commons.abtest.constant.AbTestAlgorithmEnum;
import com.sdeven.sparrow.commons.abtest.util.AbTestUitl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * DECIMAL_100 = 00~99 ,使用md5-> cityHash算法
 * </p>
 *
 * @author sdeven
 * @date 2024/6/14 10:21
 * @since 1.0.0
 */
@Service
public class Decimal100StrategyHandler implements AlgorithmStrategyHandler {


    @Override
    public String calculateSegment(String cid, String salt) {
        if (StringUtils.isNotBlank(salt)) {
            cid = salt.concat(cid);
        }
        String md5Var = AbTestUitl.computeMD5Hash(cid);
        Long cityHash64Var = AbTestUitl.computeCityHash64(md5Var);
        return AbTestUitl.getLastDigit(cityHash64Var, 2);
    }

    @Override
    public AbTestAlgorithmEnum supportType() {
        return AbTestAlgorithmEnum.DECIMAL_100;
    }
}
