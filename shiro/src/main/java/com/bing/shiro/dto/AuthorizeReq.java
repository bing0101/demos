package com.bing.shiro.dto;

import lombok.Data;

/**
 * Created by bing on 2018/2/28.
 */
@Data
public class AuthorizeReq {
    /**
     * 授权类型
     */
    private String responseType;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 重定向URI
     */
    private String redirectUri;
    /**
     * 申请的权限范围
     */
    private String scope;
    /**
     * 客户端当前状态
     */
    private String state;
}
