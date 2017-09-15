package com.bing.shardingjdbc.config;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
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
        query();
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
