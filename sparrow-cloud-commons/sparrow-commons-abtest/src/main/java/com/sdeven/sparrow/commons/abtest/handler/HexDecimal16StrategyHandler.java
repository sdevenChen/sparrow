package com.sdeven.sparrow.commons.abtest.handler;

import com.sdeven.sparrow.commons.abtest.constant.AbTestAlgorithmEnum;
import com.sdeven.sparrow.commons.abtest.util.AbTestUitl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * HEXADECIMAL_16 = 0~9~f , 使用md5算法
 * </p>
 *
 * @author sdeven
 * @date 2024/6/14 10:22
 * @since 1.0.0
 */
@Service
public class HexDecimal16StrategyHandler implements AlgorithmStrategyHandler {

    @Override
    public String calculateSegment(String cid, String salt) {
        if (StringUtils.isNotBlank(salt)) {
            cid = salt.concat(cid);
        }
        String md5Var = AbTestUitl.computeMD5Hash(cid);
        String lastVar = md5Var.substring(md5Var.length() - 1, md5Var.length());
        return lastVar;
    }

    @Override
    public AbTestAlgorithmEnum supportType() {
        return AbTestAlgorithmEnum.HEXADECIMAL_16;
    }
}
