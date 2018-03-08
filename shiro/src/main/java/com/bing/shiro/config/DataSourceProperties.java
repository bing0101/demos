package com.bing.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by bing on 2017/10/16.
 */
@ConfigurationProperties(prefix = "db")
@Component
@Data
public class DataSourceProperties {
    private String url;
    private String username;
    private String password;
}
