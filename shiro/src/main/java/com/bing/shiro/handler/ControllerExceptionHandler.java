package com.bing.shiro.handler;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by bing on 2018/2/27.
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView unauthorizedExceptionHandler(UnauthorizedException e) {
        ModelAndView modelAndView = new ModelAndView("redirect:unauthorized");
        modelAndView.addObject("unauthorizedMsg", e.getMessage());
        return modelAndView;
    }
}
