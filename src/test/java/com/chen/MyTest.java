package com.chen;

import com.chen.utils.DateTimeUtil;
import com.chen.utils.MD5Util;
import org.junit.Test;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 6:19
 */
public class MyTest {

    @Test
    public void test01(){

        // 判断失效时间
        String expireTime = "2021-05-06 10:10:10";
        System.out.println(DateTimeUtil.getSysTime());
        int result = expireTime.compareTo(DateTimeUtil.getSysTime());
        System.out.println(result);

        // 判断锁定状态
        String lockState = "0";
        if("0".equals(lockState)){
            System.out.println("账号已被锁定"); }

        // 验证ip地址
        // 浏览器的ip地址
        String ip = "192.169.1.1";
        // 允许访问的ip地址
        String ips = "192.169.1.1,192.169.1.2,192.169.1.3";
        if(ips.contains(ip)){
            System.out.println("允许访问");
        }

        // 验证密码:转换为MD5加密密文
        String pwd = "774122695CHC";
        System.out.println(MD5Util.getMD5(pwd));

    }

}
