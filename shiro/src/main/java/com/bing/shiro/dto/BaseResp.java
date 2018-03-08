package com.bing.shiro.dto;

import lombok.Data;

/**
 * Created by bing on 2018/2/28.
 */
@Data
public class BaseResp {
    private boolean success;
    private String errorCode;
    private String errorMsg;
    private Object data;
}
