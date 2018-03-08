package com.bing.shiro.dto;

import lombok.Data;

/**
 * Created by bing on 2018/2/28.
 */
@Data
public class AccessTokenReq {
    /**
     * 授权模式
     */
    private String grantType;
    /**
     * 授权码
     */
    private String authorizationCode;
    /**
     * 重定向URI
     */
    private String redirectUri;
    /**
     * 客户端ID
     */
    private String clientId;
}
