package com.bing.shardingjdbc.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.bing.shardingjdbc.mybatis.Application;
import com.bing.shardingjdbc.springboot.mapper.OrderMapper;
import com.bing.shardingjdbc.springboot.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ShardingJdbcTest {
    @Resource
    private OrderMapper orderMapper;

    @Test
    public void insert() {
        Order record = new Order();
        record.setOrderId(100L);
        record.setUserId(10);

        orderMapper.insert(record);
    }

    @Test
    public void select() {
        Order record = orderMapper.selectByPrimaryKey(1001L);
        System.out.println(JSON.toJSONString(record));
    }
}
