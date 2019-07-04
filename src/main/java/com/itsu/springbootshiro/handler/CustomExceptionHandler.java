package com.itsu.springbootshiro.handler;

import com.alibaba.fastjson.JSON;
import com.itsu.springbootshiro.util.CustomUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 苏犇
 * @date 2019/6/30 2:29
 * 自定义全局异常捕获
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    public static final String DEFAULT_ERROR_MSG = "System is busy";

    @ExceptionHandler(value = RuntimeException.class)
    public Object handlerRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error(e.getMessage());
        Object handler = null;
        if (e instanceof AuthenticationException) {
            handler = handler(request, response, e, "501");
        } else if (e instanceof AuthorizationException) {
            handler = handler(request, response, e, "502");
        } else {
            handler = handler(request, response, e, "500");
        }

        return handler;
    }


    public Object handler(HttpServletRequest request, HttpServletResponse response, Exception e, String status) {
        if (CustomUtil.isAjax(request)) {
            PrintWriter writer = null;
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                writer = response.getWriter();
                Map map = new HashMap();
                map.put("status", status);
                map.put("message", e.getMessage());
                map.put("error", DEFAULT_ERROR_MSG);
                map.put("path", request.getRequestURI());
                map.put("exception", e.getClass().getName());
                map.put("timestamp", new Date().getTime());
                writer.write(JSON.toJSONString(map));
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        } else {
            ModelAndView view = new ModelAndView("/error/500");
            view.addObject("status", status);
            view.addObject("message", e.getMessage());
            view.addObject("error", DEFAULT_ERROR_MSG);
            view.addObject("path", request.getRequestURI());
            view.addObject("exception", e.getClass().getName());
            view.addObject("timestamp", new Date());
            return view;
        }
    }

}
