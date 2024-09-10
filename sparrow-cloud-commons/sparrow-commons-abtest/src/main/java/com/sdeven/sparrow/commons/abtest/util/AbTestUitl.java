package com.sdeven.sparrow.commons.abtest.util;


import com.java.sdeven.sparrow.commons.util.CityHashUtil;
import com.sdeven.sparrow.commons.abtest.AlgorithmStrategyService;
import com.sdeven.sparrow.commons.abtest.constant.AbTestAlgorithmEnum;
import com.sdeven.sparrow.commons.abtest.handler.AlgorithmStrategyHandler;
import com.sdeven.sparrow.commons.abtest.handler.Decimal100StrategyHandler;
import com.sdeven.sparrow.commons.abtest.handler.Decimal10StrategyHandler;
import com.sdeven.sparrow.commons.abtest.handler.HexDecimal16StrategyHandler;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdeven
 * @date 2024/6/12 17:32
 * @since 1.0.0
 */
public class AbTestUitl {
    public static String computeMD5Hash(String param) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(param.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            // Ensure the hash is 32 characters long by padding with leading zeros if necessary
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }


    public static Long computeCityHash64(String param) {
        return CityHashUtil.cityHash64(param.getBytes(), 0, param.length());
    }


    public static String getLastDigit(Long param, int coord) {
        String lastStr = Long.toString(Math.abs(param));
        String var = lastStr.substring(lastStr.length() - coord, lastStr.length());
        return var;
    }

    public static void main(String[] args) {
        AlgorithmStrategyHandler handler1 = new Decimal100StrategyHandler();
        AlgorithmStrategyHandler handler2 = new Decimal10StrategyHandler();
        AlgorithmStrategyHandler handler3 = new HexDecimal16StrategyHandler();

        List<AlgorithmStrategyHandler> list = new ArrayList<>();
        list.add(handler1);
        list.add(handler2);
        list.add(handler3);
        AlgorithmStrategyService strategyService = new AlgorithmStrategyService(list);

        String cid = "4B48657AA3BA7854BE8B69EA47CE0BFC";
        String salt = "测试盐值";
        try {
            String segment = strategyService.processor(AbTestAlgorithmEnum.HEXADECIMAL_16, cid, salt);
            System.out.println(segment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
