package com.bing.shiro.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/9/8.
 */
public class RequestUtil {

    public static String getParameter(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return getRequest().getParameter(key);
    }

    public static String getRequestIp() {
        return getNginxRealIp(getRequest());
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取Nginx反向代理之后实际客户端IP
     *
     * @param request
     * @return
     */
    public static String getNginxRealIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
