package com.bing.shardingjdbc.springboot.model;

import java.io.Serializable;

public class Order implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.order_id
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    private Integer orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.user_id
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.order_id
     *
     * @return the value of order.order_id
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.order_id
     *
     * @param orderId the value for order.order_id
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.user_id
     *
     * @return the value of order.user_id
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.user_id
     *
     * @param userId the value for order.user_id
     *
     * @mbg.generated Fri Sep 15 11:56:44 CST 2017
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}