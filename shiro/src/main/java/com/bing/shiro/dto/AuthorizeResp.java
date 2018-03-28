package com.bing.shiro.dto;

import lombok.Data;

/**
 * Created by bing on 2018/2/28.
 */
@Data
public class AuthorizeResp extends BaseResp {
    /**
     * 授权码
     */
    private String code;
    /**
     * 客户端状态
     */
    private String state;
}
