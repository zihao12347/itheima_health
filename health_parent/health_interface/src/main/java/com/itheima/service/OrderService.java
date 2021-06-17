package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.Map;

public interface OrderService {
    //体检预约方法
    public Result order(Map map) throws Exception;

    //修改预约订单状态方法
    public void updateOrderStatus(Integer id, String orderStatus);

    public Map findById(Integer id) throws Exception;
}
