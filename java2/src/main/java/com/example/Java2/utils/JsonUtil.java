package com.example.Java2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json转换工具类
 *
 * @author cym    2021/12/21
 */

public class JsonUtil {

    private static  ObjectMapper objectMapper=  new ObjectMapper();

    /**
     * 将对象转换为json字符串
     * @param obj
     * @param seckillMessageClass
     * @return
     */
    public static String object2JsonStr (Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T JsonStr2object(String jsonStr,Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr,clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



}
