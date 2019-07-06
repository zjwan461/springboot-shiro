package com.itsu.springbootshiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author 苏犇
 * @date 2019/7/6 0:15
 */

public class MyTestor {
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("jack", "jack");
        System.out.println(md5Hash.toString());
    }
}
