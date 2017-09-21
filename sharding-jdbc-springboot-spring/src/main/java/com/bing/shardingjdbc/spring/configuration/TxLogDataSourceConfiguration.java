package com.bing.shardingjdbc.spring.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017/9/21.
 */
@Configuration
public class TxLogDataSourceConfiguration {

    @Bean
    public DataSource txLogDataSource(TxLogDataSourceConfigurationProperties properties) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getDriver());
        ds.setUrl(properties.getUrl());
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        return ds;
    }
}
