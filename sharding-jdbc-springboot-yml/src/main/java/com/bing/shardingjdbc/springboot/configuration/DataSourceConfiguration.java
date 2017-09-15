package com.bing.shardingjdbc.springboot.configuration;

import com.bing.shardingjdbc.springboot.algorithm.ModuloDatabaseShardingAlgorithm;
import com.bing.shardingjdbc.springboot.algorithm.ModuloTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2017/9/15.
 */
@Configuration
@Data
public class DataSourceConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public DataSource shardingDataSource(DataSourceProperties dataSourceProperties) {
        DataSourceRule dataSourceRule = new DataSourceRule(createDataSourceMap(dataSourceProperties.getDbs()));

        TableRule orderTableRule = TableRule.builder("order")
                                            .actualTables(Arrays.asList("t_order_0", "t_order_1"))
                                            .dataSourceRule(dataSourceRule).build();
        TableRule orderItemTableRule = TableRule.builder("t_order_item")
                                                .actualTables(Arrays.asList("t_order_item_0", "t_order_item_1"))
                                                .dataSourceRule(dataSourceRule).build();

        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm());
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm());

        ShardingRule shardingRule = ShardingRule.builder()
                                                .dataSourceRule(dataSourceRule)
                                                .tableRules(Arrays.asList(orderTableRule, orderItemTableRule))
                                                .databaseShardingStrategy(databaseShardingStrategy)
                                                .tableShardingStrategy(tableShardingStrategy)
                                                .build();

        DataSource shardingDataSource = null;
        try {
            shardingDataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        } catch (SQLException e) {
            logger.error("create sharding data source fail!", e);
        }

        return shardingDataSource;
    }

    private Map<String, DataSource> createDataSourceMap(List<Database> dbs) {
        if (CollectionUtils.isEmpty(dbs)) {
            logger.error("db configuration is null!");
            return null;
        }

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        for (Database db : dbs) {
            dataSourceMap.put(db.getDbname(), createDataSource(db));
        }
        return dataSourceMap;
    }

    private DataSource createDataSource(Database db) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        ds.setUrl(db.getUrl());
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());
        return ds;
    }

}
