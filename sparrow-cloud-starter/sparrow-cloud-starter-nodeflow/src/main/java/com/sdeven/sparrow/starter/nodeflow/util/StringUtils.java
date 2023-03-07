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
package com.sdeven.sparrow.starter.nodeflow.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
  * String processing tool class
  * @author sdeven
  */
public class StringUtils {
   /**
    * The encrypted string is MD5
    * @return A md5 String
    */
   public static String encryptMd5(String str) {
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
    * byte To String
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
}
