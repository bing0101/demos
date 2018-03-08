package com.bing.shiro.dto;

import lombok.Data;

/**
 * Created by bing on 2018/2/28.
 */
@Data
public class AccessTokenResp extends BaseResp {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 令牌类型
     */
    private String tokenType;
    /**
     * 过期时间
     */
    private Integer expiresIn;
    /**
     * 更新令牌
     */
    private String refreshToken;
    /**
     * 权限范围
     */
    private String scope;
}
