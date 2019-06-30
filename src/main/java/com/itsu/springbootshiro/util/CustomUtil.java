package com.itsu.springbootshiro.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 苏犇
 * @date 2019/6/30 2:34
 */

public class CustomUtil {

    public static boolean isAjax(HttpServletRequest request) {
        if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            return true;
        }
        return false;
    }
}
