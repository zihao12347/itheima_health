package com.heima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SmsUtil;
import com.itheima.utils.ValidateCodeUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 体检预约控制类
 */
@RestController
@RequestMapping("/order")
public class OrderContronller {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    /**
     * 完成体检预约设置的方法
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result Order(@RequestBody Map map) {
        //1.判断用户输入的验证码是否跟发送验证码时保存在redis中的验证码一致
        String telephone = (String) map.get("telephone");
        String RedisValidCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if (RedisValidCode == null || !RedisValidCode.equals(validateCode)) {//当验证码不一致表示验证码输入错误或者验证码有效时超时
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        } else {

            //2.执行预约设置
            Result result = new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            try {
                map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型
                result = orderService.order(map);
            } catch (Exception e) {
                return result;
            }
            if (result.isFlag()) {//根据flag属性的值判断是否成功完成体检预约设置
                //3.完成预约设置发送成功预约信息给用户
                HashMap<String, String> susInfoMap = new HashMap<>();
                String orderDate = (String) map.get("orderDate");
                susInfoMap.put("orderDate", orderDate);
                String name = (String) map.get("name");
                susInfoMap.put("name", name);
                String helpCode = (String) map.get("helpCode");
                susInfoMap.put("helpCode",helpCode );
                SmsUtil.sendSuccessMessage(telephone, susInfoMap);
            }
            return result;
        }
    }

    /**
     * 根据orderId预约id查询成功预约数据回显：包括体检人信息，套餐信息,预约信息
     * @param id
     * @return  根据前台的数据类型需要，当前使用map来封装前台需要的数据，进行返回
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = this.orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_SUCCESS);
        }

    }
}
