package com.bing.shardingjdbc.mybatis.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */
@Component
@ConfigurationProperties
@Data
public class DataSourceProperties {
    private List<Database> dbs;
}
