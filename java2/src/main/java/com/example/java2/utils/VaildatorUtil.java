//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.Java2.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VaildatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");

    public VaildatorUtil() {
    }

    public static boolean isMobile(String mobbile) {
        if (StringUtils.isEmpty(mobbile)) {
            return false;
        } else {
            Matcher matcher = mobile_pattern.matcher(mobbile);
            return matcher.matches();
        }
    }
}
