package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)   //发布服务
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    //注入dao
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入excel中设置的预约设置数据：
     *
     * @param list  当前方法的参数为OrderSetting类型的List集合，
     */
    @Override
    public void add(List<OrderSetting> list) {
            //判断list是否为空
        if (list!=null&&list.size()>0) {
            //遍历list集合
            for (OrderSetting orderSetting : list) {
                //1.先根据orderSetting中保存的预约设置日期进行查询，如果该时期已存在预约设置信息，则修改更新预约设置信息，没有直接保存
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate>0){
                    //1.如果查询到某日期已存在预约设置的信息，则修改该日期预约设置的数量
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //当不存在时，直接保存预约设置的信息
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
    /**
     * 根据当前页面显示的的月份查询对应的预约数据
     * @param date
     * @return
     */

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //1.封装查询条件
        HashMap<String, String> map = new HashMap<>();
        //每月第一天，都是1号
        String begining =date+"-1";
        map.put("begining", begining);
        //每月最后一天，最多31天，
        String end=date+"-31";
        map.put("end", end);
        //2.调用根据月份查询预约信息方法
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByMonth(map);
        //3.封装查询结果结
        /*      页面数据格式
         this.leftobj = [
                    { date: 1, number: 120, reservations: 1 },
                    { date: 3, number: 120, reservations: 119 },
                    { date: 4, number: 120, reservations: 120 },
                    { date: 6, number: 120, reservations: 1 },
                    { date: 8, number: 120, reservations: 1 }
                ];*/
        //创建类型为map的List集合，用于封装查询数据
        ArrayList<Map> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderSettings)){//当orderSettings不为空时，遍历orderSettings数据
            //将查询的结果进行封装
            for (OrderSetting orderSetting : orderSettings) {
                HashMap<String, Object> m = new HashMap<>();
                m.put("date", orderSetting.getOrderDate().getDate());//获取日期数字
                m.put("number", orderSetting.getNumber());
                m.put("reservations", orderSetting.getReservations());
                list.add(m);
            }

        }
        for (Map map1 : list) {
           map1.get("date");
            map1.get("number");
        }
        //将封装后的查询结果集放回
        return list;
    }


    /**
     * 根据日期设置某日期可预约的人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting){
       //1.根据日期查询当天是否已经设置了预约人数
        OrderSetting byOrderDate = this.orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if (byOrderDate!=null){//当查询结果大于0，表示当前已经设置了，调用更新方法重新设置可预约人数
            if (orderSetting.getNumber()>=byOrderDate.getReservations()){//只有当前台设置的可预约人数大于或者等于数据库的已预约人数才有效果
                this.orderSettingDao.editNumberByDate(orderSetting);
            }else {
               throw new RuntimeException("预约设置不符合规则！");
            }

        }else {//当前日期没有设置预约人数，则调用新增方法
            this.orderSettingDao.add(orderSetting);
        }

    }
}
