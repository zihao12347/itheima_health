package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置dao
 */
public interface OrderSettingDao {
    //保存预约设置信息
    @Insert("insert into t_ordersetting (orderDate,number,reservations)values(#{orderDate},#{number},#{reservations})")
    public void add(OrderSetting orderSetting);
    //根据日期查询该时期是否已经在预约设置信息
    @Select("select count(*) from t_ordersetting where orderDate=#{orderDate}")
    public long findCountByOrderDate(Date orderDate);
    //修改某日期的预约设置的信息
    @Update("update t_ordersetting set number=#{number} where orderDate=#{orderDate}")
    public void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据当前页面的月份查询预约信息，显示到页面
     *      between and 范围查询：
     *          1.用于时间范围查询，当前事情between and进行月份日期范围查询
     *          2.用于数字范围查询
     */
    @Select("select * from t_ordersetting where orderDate between #{begining} and  #{end}")
    public List<OrderSetting> getOrderSettingByMonth(Map map);

    @Update("update t_ordersetting set number=#{number} where orderDate = #{orderDate}")
    public void editNumberByDate(OrderSetting orderSetting);


    //查询用户预约日期是否已经进行了预约设置
    @Select("select * from t_ordersetting where orderDate = #{orderDate}")
    public OrderSetting findByOrderDate(Date orderDate);

    @Update(" update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    public void editReservations(OrderSetting orderSetting);

}
