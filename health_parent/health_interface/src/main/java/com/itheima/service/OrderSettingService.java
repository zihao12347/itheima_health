package com.itheima.service;

import com.itheima.pojo.OrderSetting;


import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    /**
     * 批量导入预约设置信息
     * @param list  当前方法的参数为OrderSetting类型的List集合，
     *              由于是由excel文件进行批量导入到服务端的数据，所以保存到数据库的数据应为list集合数据
     */
    public void add(List<OrderSetting> list);

    public List<Map> getOrderSettingByMonth(String date);

    public void editNumberByDate(OrderSetting orderSetting);


}
