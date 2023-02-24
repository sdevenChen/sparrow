package com.sdeven.sparrow.starter.nodeflow.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
  * @Description 字符串处理工具类
  * @Author sdeven.chen.dongwei@gmail.com
  * @Create 12/12/20 10:20
  */
public class StringUtils {
   /**
    * @return
    * @Description 字符串加密
    * @Date 12/10/20 09:18
    * @Param
    */
   public static String getMd5(String str) {
       String returnStr = null;
       try {
           MessageDigest md = MessageDigest.getInstance("SHA");
           md.update(str.getBytes());
           byte b[] = md.digest();
           returnStr = byte2String(b);
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return returnStr;
   }

   public static boolean isotEmpty(String str) {
       boolean flag = false;
       if (str != null && !"".equals(str)) {
           flag = true;
       }

       return flag;
   }

   /**
    * @return
    * @Description byte 转 String
    * @Date 12/10/20 09:18
    * @Param
    */
   public static String byte2String(byte[] b) {
       String hash = "";
       for (int i = 0; i < b.length; i++) {
           int temp;
           if (b[i] < 0) {
               temp = 256 + b[i];
           } else {
               temp = b[i];
           }
           if (temp < 16) {
               hash += "0";
           }
           hash += Integer.toString(temp, 16);
       }
       hash = hash.toUpperCase();
       return hash;
   }

   public static String baseString(BigInteger num, int base) {
       String str = "",
               digit = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+|?/.,':[]{}";
       if (num.shortValue() == 0) {
           return "";
       } else {
           BigInteger valueOf = BigInteger.valueOf(base);
           str = baseString(num.divide(valueOf), base);
           return str + digit.charAt(num.mod(valueOf).shortValue());
       }
   }



   /**
    * 判断字符串是否为空
    *
    * @param str 字符串
    * @return 是否为空
    */
   public static boolean isEmpty(String str) {
       return (str == null || str.trim().length() == 0);
   }

   public static boolean isNotEmpty(String str) {
       return !StringUtils.isEmpty(str);
   }

   public static boolean isEmpty(Object str) {
       return (str == null || str.toString().trim().length() == 0);
   }

   public static boolean isNotEmptyOrNvlStr(String str) {
       return !(str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str));
   }

   public static boolean isNotEmptyOrNvlObj(Object str) {
       return !(str == null || str.toString().trim().length() == 0 || "null".equalsIgnoreCase(str.toString()));
   }
   public static void main(String[] args) {
       BigInteger num = BigInteger.valueOf(200000L);
       String str = baseString(num, 10);
       System.out.println(str);

       int i = 123;
       java.text.DecimalFormat df = new java.text.DecimalFormat("0000000000");
       System.out.println(df.format(i));
   }
}
