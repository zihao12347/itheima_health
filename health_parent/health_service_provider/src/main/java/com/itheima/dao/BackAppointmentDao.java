package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.pojo.bo.BoOrder;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface BackAppointmentDao {
    @Select("<script>"
            + "select o.id, m.name , m.phoneNumber, o.orderDate as date, o.orderType, o.orderStatus, s.name as sname  " +
            "from t_member m, t_order o,t_setmeal s "
            + "where 1=1 "
            + "and m.id=o.member_id and s.id=o.setmeal_id "
            + "<if test='startDate != null and startDate.length > 0'>"
            + "and o.orderDate &gt;= #{startDate}"
            + "</if>"
            + "<if test='endDate != null and endDate.length > 0 '>"
            + "and o.orderDate &lt;= #{endDate}"
            + "</if>"
            + "<if test='orderType != null and orderType.length > 0'>"
            + "and o.orderType like '%${orderType}%'"
            + "</if>"
            + "<if test='orderStatus != null and orderStatus.length > 0'>"
            + "and o.orderStatus like '%${orderStatus}%'"
            + "</if>"
            + "<if test='nameOrphoneNumber != null and nameOrphoneNumber.length > 0'>"
            + "and m.name  like '%${nameOrphoneNumber}%' or m.phoneNumber like '%${nameOrphoneNumber}%'"
            + "</if>"
            + "</script>")
    public Page<BoOrder> findByCondition(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("orderType") String orderType,
                                         @Param("orderStatus") String orderStatus, @Param("nameOrphoneNumber") String nameOrphoneNumber);

    @Delete("delete from t_order where id=#{id}")
    public void delete(Integer id);
}
