//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.Java2.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    private static String salt = "1a2b3c4d";

    public MD5Util() {
    }

    public static String md5(String src) {
        String md5 = DigestUtils.md5Hex(src);
        return md5;
    }

    public static String inputPassToFromPass(String inputPass) {
        String str = "" + salt.charAt(1) + salt.charAt(6) + inputPass + salt.charAt(2) + salt.charAt(3);
        return md5(str);
    }

    public static String fromPassToDBPass(String fromPass, String salt) {
        String str = "" + salt.charAt(1) + salt.charAt(6) + fromPass + salt.charAt(2) + salt.charAt(3);
        return md5(str);
    }

    public static String inputPassToDBPss(String inputPass, String salt) {
        String fromPass = inputPassToFromPass(inputPass);
        return fromPassToDBPass(fromPass, salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(fromPassToDBPass("d3cae5cc6c8cf5776fbd1fb351ec677e", "1a2b3c4d"));
        System.out.println(inputPassToDBPss("123456", "1a2b3c4d"));
    }
}
