package com.itheima.dao;


import com.itheima.entity.PageResult;
import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约设置dao接口
 */
public interface OrderDao {

    /**
     * 根据用户id,会员id，用户选择的预约日期，查询用户是否重复预约
     */
    @Select("select * from t_order where member_id=#{memberId} and orderDate=#{orderDate} and setmeal_id=#{setmealId}")
    public List<Order> findByCondition(Integer memberId, Date orderDate, Integer setmealId);

    /**
     * 预约设置
     */

    @Insert(" insert into  t_order (member_id,orderDate,orderType,orderStatus,setmeal_id) values (#{memberId},#{orderDate},#{orderType},#{orderStatus},#{setmealId})")
    @SelectKey(statement = {"select last_insert_id()"},keyProperty = "id",keyColumn = "id",before = false,resultType =Integer.class )
    void add(Order order);

    /**
     * 根据orderId预约id查询成功预约数据回显：包括体检人信息，套餐信息,预约信息
     * @param id
     * @return
     */
    @Select("select m.name  member, s.name setmeal,o.orderDate orderDate, o.orderType orderType " +
            "from t_member m,t_setmeal s,t_order o " +
            "where o.member_id=m.id and o.setmeal_id=s.id and o.id = #{id}")
    public Map findById(Integer id);
    @Select("select count(id) from t_order where orderDate =#{date}")
    public Integer findOrderCountByDate(String date);//当前日的预约数量
    @Select("select count(id) from t_order where orderDate >= #{date}")
    public Integer findOrderCountAfterDate(String date);//当前日期，已预约的数量
    @Select("select count(id) from t_order where orderDate  = #{date} and orderStatus ='已到诊'  ")
    public Integer findVisitsCountByDate(String date);//当前日到诊数量
    @Select("select count(id) from t_order where orderDate >= #{date} and  orderStatus='已到诊' ")
    public Integer findVisitsCountAfterDate(String date);//当前日期，已到诊的数量
    //热门套餐，查询前4条
    @Select("SELECT s.name,COUNT(o.id) setmeal_count ,COUNT(o.id)/(select COUNT(id)from t_order) proportion " + //前台页面需要什么数据，sql就查询什么数据
            "FROM t_order o,t_setmeal s " +
            "WHERE o.setmeal_id=s.id " +
            "GROUP BY o.setmeal_id " +  //根据预约的数量进行排序
            "ORDER BY setmeal_count DESC LIMIT 0, 4")   //热门套餐，只需要显示前4条热门套餐数据
    public List<Map<String,Object>> findHotSetmeal();

    @Update("update t_order set orderStatus = #{orderStatus} WHERE id=#{id}")
    public void updateOrderState(@Param("id") Integer id,@Param("orderStatus") String orderStatus);
}
