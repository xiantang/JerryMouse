package com.github.apachefoundation.jerrymouse.utils;

/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class StringUtils {
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null && str.length() == 0;
    }
}
