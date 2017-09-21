package com.bing.shardingjdbc.spring.configuration;

import com.dangdang.ddframe.rdb.sharding.jdbc.core.datasource.ShardingDataSource;
import com.dangdang.ddframe.rdb.transaction.soft.api.SoftTransactionManager;
import com.dangdang.ddframe.rdb.transaction.soft.api.config.NestedBestEffortsDeliveryJobConfiguration;
import com.dangdang.ddframe.rdb.transaction.soft.api.config.SoftTransactionConfiguration;
import com.dangdang.ddframe.rdb.transaction.soft.bed.BEDSoftTransaction;
import com.dangdang.ddframe.rdb.transaction.soft.constants.SoftTransactionType;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/9/21.
 */
@Configuration
public class ShardingJdbcTxConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public BEDSoftTransaction bedSoftTransaction(ShardingDataSource shardingDataSource, DataSource txLogDataSource) {
        SoftTransactionManager txManager = new SoftTransactionManager(getTxConfiguration(shardingDataSource, txLogDataSource));
        try {
            txManager.init();
        } catch (SQLException e) {
            logger.error("sharding-jdbc tx manager init error!", e);
            return null;
        }
        return (BEDSoftTransaction) txManager.getTransaction(SoftTransactionType.BestEffortsDelivery);
    }

    private SoftTransactionConfiguration getTxConfiguration(final DataSource dataSource, final DataSource txLogDataSource) {
        SoftTransactionConfiguration txConfig = new SoftTransactionConfiguration(dataSource);
        txConfig.setSyncMaxDeliveryTryTimes(5);//设置同步事务送达的最大尝试次数

        // 设置存储事务日志的数据库，默认的存储类型为RDB，see -> SoftTransactionConfiguration.storageType line62
        txConfig.setTransactionLogDataSource(txLogDataSource);

        // 使用内嵌异步作业，仅用于开发环境
        // 内嵌了一个注册中心，默认zookeeperPort 4181
        NestedBestEffortsDeliveryJobConfiguration nestedJobConfig = new NestedBestEffortsDeliveryJobConfiguration();
        txConfig.setBestEffortsDeliveryJobConfiguration(Optional.of(nestedJobConfig));

        return txConfig;
    }
}
