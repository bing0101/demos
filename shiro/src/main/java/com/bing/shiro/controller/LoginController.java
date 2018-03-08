package com.bing.shiro.controller;

import com.bing.shiro.dto.LoginReq;
import com.bing.shiro.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bing on 2018/2/24.
 */
@RestController
@RequestMapping("/shiro")
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/login")
    public String login(LoginReq loginReq) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginReq.getUsername(), loginReq.getPassword());
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            return "账号不存在";
        } catch (IncorrectCredentialsException e) {
            return "密码不正确";
        } catch (Exception e) {
            logger.error("系统异常", e);
            return "系统异常";
        }

        return "success";
    }

    @RequestMapping("/logout")
    public void logout() {
        SecurityUtils.getSubject().logout();

    }

    @RequiresPermissions("index")
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized() {
        String unauthorizedMsg = RequestUtil.getParameter("unauthorizedMsg");
        if (StringUtils.isBlank(unauthorizedMsg)) {
            unauthorizedMsg = "unauthorized";
        }

        return unauthorizedMsg;
    }

    @RequiresRoles("12")
    @RequestMapping("/role1")
    public String role1() {
        return "role1";
    }

    @RequiresGuest
    @RequestMapping("/guest")
    public String guest() {
        return "guest";
    }

}
