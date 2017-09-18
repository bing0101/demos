package com.bing.shardingjdbc.springboot.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_item.item_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    private Long itemId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_item.order_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    private Integer orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_item.user_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_item
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_item.item_id
     *
     * @return the value of order_item.item_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_item.item_id
     *
     * @param itemId the value for order_item.item_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_item.order_id
     *
     * @return the value of order_item.order_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_item.order_id
     *
     * @param orderId the value for order_item.order_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_item.user_id
     *
     * @return the value of order_item.user_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_item.user_id
     *
     * @param userId the value for order_item.user_id
     *
     * @mbg.generated Mon Sep 18 16:07:00 CST 2017
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}