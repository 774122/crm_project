package com.chen.exception;

import com.chen.exception.Login.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/8 0008 - 上午 11:57
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理登录异常
     * @param ex：传入的异常对象
     * @return ：视图，转入异常处理页面
     */
    @ExceptionHandler(value= LoginException.class)
    @ResponseBody
    public Map<String, String> doLoginException(Exception ex){
        Map<String,String> result = new HashMap<>();
        result.put("msg", ex.getMessage());
        return result;
    }

    @ExceptionHandler(value= Exception.class)
    public ModelAndView doOtherException(Exception ex){
        ModelAndView mv = new ModelAndView();
        mv.addObject("tips","@ConretollerAdvice使用注解处理其他异常");
        mv.addObject("ex",ex);
        mv.setViewName("otherError");
        return mv;
    }
}
