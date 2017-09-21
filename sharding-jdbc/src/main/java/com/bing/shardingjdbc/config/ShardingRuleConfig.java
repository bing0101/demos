package com.bing.shardingjdbc.config;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.transaction.soft.api.SoftTransactionManager;
import com.dangdang.ddframe.rdb.transaction.soft.api.config.NestedBestEffortsDeliveryJobConfiguration;
import com.dangdang.ddframe.rdb.transaction.soft.api.config.SoftTransactionConfiguration;
import com.dangdang.ddframe.rdb.transaction.soft.bed.BEDSoftTransaction;
import com.dangdang.ddframe.rdb.transaction.soft.constants.SoftTransactionType;
import com.dangdang.ddframe.rdb.transaction.soft.constants.TransactionLogDataSourceType;
import com.google.common.base.Optional;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 */
public class ShardingRuleConfig {
    private static Logger logger = LoggerFactory.getLogger(ShardingRuleConfig.class);

    public static void main(String[] args) {
        tx();
    }

    private static void tx() {
        String sql1 = "insert into t_order (order_id, user_id) values (?, ?)";
        String sql2 = "insert into t_order_item (item_id, order_id, user_id) values (?, ?, ?)";

        DataSource dataSource = getDataSource();

        SoftTransactionConfiguration txConfig = getTxConfiguration(dataSource, false, true);
        SoftTransactionManager txManager = new SoftTransactionManager(txConfig);

        Connection connection = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        BEDSoftTransaction tx = null;
        try {
            connection = dataSource.getConnection();
            ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, 1002);
            ps1.setInt(2, 11);

            ps2 = connection.prepareStatement(sql2);
            ps2.setString(1, "11121ds");
            ps2.setInt(2, 1002);
            ps2.setInt(3, 11);

            txManager.init();
            tx = (BEDSoftTransaction) txManager.getTransaction(SoftTransactionType.BestEffortsDelivery);
            tx.begin(connection);
            int count1 = ps1.executeUpdate();
            int count2 = ps2.executeUpdate();

            logger.info("count1 : {}, count2 : {}", count1, count2);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                tx.end();
                ps1.close();
                ps2.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static SoftTransactionConfiguration getTxConfiguration(final DataSource dataSource,boolean useMemoryStorage, boolean useNestedJob) {
        SoftTransactionConfiguration txConfig = new SoftTransactionConfiguration(dataSource);
        txConfig.setSyncMaxDeliveryTryTimes(5);//设置同步事务送达的最大尝试次数
        if (useMemoryStorage) {//使用内存存储事务日志
            txConfig.setStorageType(TransactionLogDataSourceType.MEMORY);
        } else {//使用RDB存储事务日志
            //设置存储事务日志的数据库，默认的存储类型为RDB，see -> SoftTransactionConfiguration.storageType line62
            txConfig.setTransactionLogDataSource(createDataSource("tx_log"));
        }

        if (useNestedJob) {//使用内嵌异步作业，仅用于开发环境
            // 内嵌了一个注册中心，默认zookeeperPort 4181
            NestedBestEffortsDeliveryJobConfiguration nestedJobConfig = new NestedBestEffortsDeliveryJobConfiguration();
            txConfig.setBestEffortsDeliveryJobConfiguration(Optional.of(nestedJobConfig));
        }

        return txConfig;
    }

    private static void insert() {
        String sql = "insert into t_order (order_id, user_id) values (?, ?)";

        DataSource dataSource = getDataSource();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 1002);
            ps.setInt(2, 11);

            int count = ps.executeUpdate();
            logger.info("count : {}", count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void query2() {
        DataSource dataSource = getDataSource();
        String sql = "SELECT * FROM t_order WHERE user_id=? ";

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 10);

            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                ps.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void query() {
        DataSource dataSource = getDataSource();
        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND i.order_id=? AND o.order_id=?";

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 10);
            ps.setInt(2, 1001);
            ps.setInt(3, 1001);

            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt(2));
                System.out.println(rs.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                ps.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static DataSource getDataSource() {
        DataSourceRule dataSourceRule = new DataSourceRule(createDateSourceMap());

        TableRule orderTableRule = TableRule.builder("t_order")
                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
                .dataSourceRule(dataSourceRule).build();
        TableRule orderItemRule = TableRule.builder("t_order_item")
                .actualTables(Arrays.asList("t_order_item_0", "t_order_item_1"))
                .dataSourceRule(dataSourceRule).build();

        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm());
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm());

        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(orderTableRule, orderItemRule))
                .databaseShardingStrategy(databaseShardingStrategy)
                .tableShardingStrategy(tableShardingStrategy)
                .build();

        DataSource dataSource = null;
        try {
            dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    private static Map<String, DataSource> createDateSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds_0", createDataSource("sharding_jdbc_0"));
        dataSourceMap.put("ds_1", createDataSource("sharding_jdbc_1"));

        return dataSourceMap;
    }

    private static DataSource createDataSource(final String dataSourceName) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        ds.setUrl(String.format("jdbc:mysql://120.25.73.72:3306/%s", dataSourceName));
        ds.setUsername("xiaobing");
        ds.setPassword("8541868");
        return ds;
    }

}
