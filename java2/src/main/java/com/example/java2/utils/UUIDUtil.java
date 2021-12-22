//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.java2.utils;

import java.util.UUID;

public class UUIDUtil {
    public UUIDUtil() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
