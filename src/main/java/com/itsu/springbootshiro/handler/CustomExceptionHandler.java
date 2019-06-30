package com.itsu.springbootshiro.handler;

import com.alibaba.fastjson.JSON;
import com.itsu.springbootshiro.util.CustomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 苏犇
 * @date 2019/6/30 2:29
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = RuntimeException.class)
    public Object handlerRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return null;
    }

    @ExceptionHandler(value = Exception.class)
    public Object handlerJedisExcepton(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error(e.getMessage());
        if (CustomUtil.isAjax(request)) {
            PrintWriter writer = null;
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                writer = response.getWriter();
                Map map = new HashMap();
                map.put("code", "501");
                map.put("msg", e.getMessage());
                writer.write(JSON.toJSONString(map));
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        } else {
            ModelAndView view = new ModelAndView("/error/500");
            view.addObject("code", "501");
            view.addObject("msg", e.getMessage());
            return view;
        }
    }

}
