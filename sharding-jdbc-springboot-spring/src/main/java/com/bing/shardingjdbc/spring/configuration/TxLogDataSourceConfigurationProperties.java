package com.bing.shardingjdbc.spring.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/9/21.
 */
@Component
@Data
@ConfigurationProperties(prefix = "tx_log")
public class TxLogDataSourceConfigurationProperties {
    private String driver;
    private String url;
    private String username;
    private String password;
}
