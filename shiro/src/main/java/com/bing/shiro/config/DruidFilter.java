package com.bing.shiro.config;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by dwb on 2017/6/1.
 */
@Component
public class DruidFilter extends FilterEventAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ConnectionProxy connection_connect(FilterChain chain, Properties info) throws SQLException {
        long startTime = System.currentTimeMillis();
        ConnectionProxy connection = super.connection_connect(chain, info);
        logger.info("create connection cost:" + (System.currentTimeMillis() - startTime) + " ms");
        return connection;
    }

    @Override
    public boolean preparedStatement_execute(FilterChain chain, PreparedStatementProxy statement) throws SQLException {
        long startTime = System.currentTimeMillis();
        boolean firstResult = super.preparedStatement_execute(chain, statement);
        long cost = System.currentTimeMillis() - startTime;
        if (cost > 100) {
            logger.info("execute cost time:" + cost + " ms");
        }
        return firstResult;
    }
}
