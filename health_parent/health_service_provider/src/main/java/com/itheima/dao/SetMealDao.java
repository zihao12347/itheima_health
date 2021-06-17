package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface SetMealDao {
    //新增套餐
    @Insert("insert into t_setmeal" +
            "            (code,name,sex,age,helpCode,price,remark,attention,img)" +
            "                values" +
            "            (#{code},#{name},#{sex},#{age},#{helpCode},#{price},#{remark},#{attention},#{img})")
    @SelectKey(statement =  {"select last_insert_id()"}, keyColumn = "id", before = false, resultType = Integer.class, keyProperty = "id")
    public void add(Setmeal setmeal);

    //设置套餐与检查组的关联关系
    @Insert("insert into t_setmeal_checkgroup (setmeal_id,checkgroup_id)values(#{setmealid},#{checkgroupid})")
    public void setAssociation(Map map);

    //分页查询
    @Select("<script>"
            + "select * from t_setmeal where 1=1"
            + "<if test='queryString != null and queryString.length > 0'>"
            + "and code like '%${queryString}%' or name like '%${queryString}%' or helpCode like '%${queryString}%'"
            + "</if>"
            + "</script>")
    public Page<Setmeal> findPage(@Param("queryString") String queryString);


    //查询套餐列表数据
    @Select("select * from t_setmeal")
    public List<Setmeal> findAll();


    /**根据套餐id查询套详情数据
     *
     *
     *
     *
     *
     * @param id
     * @return
     */
    @Select("select * from t_setmeal  where id=#{id}")
    @Results({
            @Result(id = true,column= "id",property = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "code",column = "code"),
            @Result(property = "helpCode",column = "helpCode"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "age",column = "age"),
            @Result(property = "price",column = "price"),
            @Result(property = "remark",column = "remark"),
            @Result(property = "attention",column = "attention"),
            @Result(property = "img",column = "img"),
            //多对多查询：套餐  <---> 检查组
            @Result(property = "checkGroups",column = "id",javaType = List.class,many = @Many(select = "com.itheima.dao.CheckGroupDao.findBySetmealId"))
    })
    public Setmeal findMealDeatailById(Integer id);


    @Select("select * from t_setmeal  where id=#{id}")
    public Setmeal findById(Integer id);

    /**
     * 查询套餐预约占比数：1.套餐表，2.预约表
     *      1.查询套餐的名称
     *      2.查询每个套餐的预约数量：
     *          根据套餐名称分组查询，出当前套餐套餐的数量
     *     返回数据类型为List<Map<String,Object>
     *          s.name  name
     *          将Count(o.id),使用as 起别名为 value
     *          封装后也就是map<name,value>
     */
    @Select("select s.name,COUNT(o.id) as value  from t_order o,t_setmeal s WHERE o.setmeal_id =s.id GROUP BY s.name")
    public List<Map<String,Object>> findSetmealCount();

}
