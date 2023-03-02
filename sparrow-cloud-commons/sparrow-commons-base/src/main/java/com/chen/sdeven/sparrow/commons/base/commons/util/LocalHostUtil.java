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
package com.chen.sdeven.sparrow.commons.base.commons.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description IP工具包
 * @Author sdeven
 * @Create 12/18/20 16:51
 */
public class LocalHostUtil {

    private static String localHostAddress = "";
    private static String ipCode = "";

    /**
     * @return String
     * @Description 获取IpCode 不带"."
     * @Date 12/18/20 16:48
     */
    public static String getIpCode() {
        if (ipCode != null && !ipCode.equals("")) {
            return ipCode;
        }
        synchronized (LocalHostUtil.class) {
            if (ipCode != null && !ipCode.equals("")) {
                return ipCode;
            }
            String ip = getLocalHostAddress();
            String[] ipArray = ip.split("\\.");
            for (int j = 0; j < 4; j++) {
                for (int i = ipArray[j].length(); i < 3; i++) {
                    ipCode = ipCode + "0";
                }
                ipCode = ipCode + ipArray[j];
            }
        }
        return ipCode;
    }

    /**
     * @return
     * @Description 取本机IP地址。配置：本机IP地址 -Djava.net.preferIPv4Stack=TRUE
     * @Date 12/18/20 16:52
     * @Param
     */
    @SuppressWarnings("rawtypes")
	public static String getLocalHostAddress() {
        if (!localHostAddress.equals("")) {
            return localHostAddress;
        }
        String ip = "";
        String ipBak = "";
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
            if (StringUtils.isNotBlank(ip) && !"127.0.0.1".equals(ip) && ip.indexOf(':') < 0) {
                localHostAddress = ip;
                return ip;
            }
            Enumeration netInterfaces = null;
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress iAddress = null;
            out:
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    iAddress = inetAddresses.nextElement();
                    if (!iAddress.isSiteLocalAddress()
                            && !iAddress.isLoopbackAddress()
                            && iAddress.getHostAddress().indexOf(':') == -1) {
                        ip = iAddress.getHostAddress();
                        break out;
                    } else {
                        ip = iAddress.getHostAddress();
                        if (!ip.equals("127.0.0.1") && ip.split("\\.").length == 4
                                && ip.indexOf(':') < 0) {
                            ipBak = ip;
                        }
                        ip = "";
                        iAddress = null;
                    }
                }
            }
            if (!ip.equals("127.0.0.1") && ip.split("\\.").length == 4
                    && ip.indexOf(':') < 0) {
                localHostAddress = ip;
                return ip;
            }
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
                    .getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if (!ni.getName().equals("eth0") && !ni.getName().equals("eth1") && !ni.getName().equals("bond0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address) {
                            continue;
                        }
                        ip = ia.getHostAddress();
                        if (!ia.isSiteLocalAddress() && !ip.equals("127.0.0.1") && ip.split("\\.").length == 4 && ip.indexOf(':') < 0) {
                            localHostAddress = ip;
                            return ip;
                        }

                        if (ni.getName().equals("eth1")
                                && !ia.isSiteLocalAddress()
                                && !ip.equals("127.0.0.1")
                                && ip.split("\\.").length == 4
                                && ip.indexOf(':') < 0) {
                            ipBak = ip;
                            ip = "";
                        }
                    }
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (!ip.equals("127.0.0.1") && ip.split("\\.").length == 4 && ip.indexOf(':') < 0) {
            localHostAddress = ip;
            return ip;
        }
        if (!ipBak.equals("127.0.0.1") && ipBak.split("\\.").length == 4 && ipBak.indexOf(':') < 0) {
            localHostAddress = ipBak;
            return ipBak;
        }
        localHostAddress = ip;
        return ip;
    }

    public static void main(String[] args) {
        System.out.println(getLocalHostAddress());
    }
}
