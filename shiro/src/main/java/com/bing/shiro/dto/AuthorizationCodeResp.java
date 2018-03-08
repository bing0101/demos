package com.bing.shiro.dto;

import lombok.Data;

/**
 * Created by bing on 2018/2/28.
 */
@Data
public class AuthorizationCodeResp extends BaseResp {
    /**
     * 重定向URI
     */
    private String redirectUri;
    /**
     * 授权码
     */
    private String authorizationCode;
    /**
     * 客户端状态
     */
    private String state;
}
