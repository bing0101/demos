package com.bing.shardingjdbc.spring;

import com.alibaba.fastjson.JSON;
import com.bing.shardingjdbc.springboot.mapper.OrderItemMapper;
import com.bing.shardingjdbc.springboot.mapper.OrderMapper;
import com.bing.shardingjdbc.springboot.model.Order;
import com.bing.shardingjdbc.springboot.model.OrderExample;
import com.bing.shardingjdbc.springboot.model.OrderItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ShardingJdbcTest {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Test
    public void insert() {
        Order record = new Order();
//        record.setOrderId(200);
        record.setUserId(10);

        orderMapper.insertSelective(record);
    }

    @Test
    public void selectByUserId() {
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdEqualTo(10);
        List<Order> orders = orderMapper.selectByExample(example);
        System.out.println(JSON.toJSONString(orders));
    }

    @Test
    public void select() {
        Order record = orderMapper.selectByPrimaryKey(1001L);
        System.out.println(JSON.toJSONString(record));
    }

    @Test
    public void itemInsert() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(100);
        orderItem.setUserId(10);

        orderItemMapper.insertSelective(orderItem);
    }
}
