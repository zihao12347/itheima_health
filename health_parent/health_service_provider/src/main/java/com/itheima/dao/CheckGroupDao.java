package com.itheima.dao;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    /**
     * 新增检查组方法
     *
     * @param checkGroup
     * @SelectKey注解：获取新增数据的主键 我们在插入数据的时候，希望插入完成返回插入的数据的主键，我们可以使用@SelectKey注解来完成
     * keyProperty属性：将查询结果赋值给pojo对象的那个属性
     * statement属性：查询语句，当获取新增数据的主键时，使用“select last_insert_id”语句
     * keyColumn属性：将查询结果赋值给数据库表中的哪一列
     * resultType属性：返回结果类型
     * before属性：true表示在插入之前执行，false表示在插入之后执行
     */
    @Insert("insert into  t_checkgroup(code,name,sex,helpCode,remark,attention)" +
            "            values " +
            "        (#{code},#{name},#{sex},#{helpCode},#{remark},#{attention})")
    @SelectKey(statement = {"select last_insert_id()"}, keyProperty = "id", keyColumn = "id", before = false, resultType = Integer.class)
    void add(CheckGroup checkGroup);


    /**
     * 设置检查项和检查组的关联关系
     *
     * @param map 以key-values的形式封装了checkGroup的id和checkItem的id
     */

    @Insert("insert into  t_checkgroup_checkitem(checkgroup_id,checkitem_id) " +
            "            values" +
            "        (#{checkgroup_id},#{checkitem_id})")
    void setCheckGroupAndCheckItem(Map map);

    /**
     * 动态分页查询
     *
     * @param queryString
     * @return
     *      @param注解的使用：
     *                  动态sql中使用参数作为比变量时，使用@param注解
     */
    @Select("<script>"
            + "select * from t_checkgroup where 1=1"
            + "<if test='queryString != null and queryString.length > 0'>"
            + "and code like '%${queryString}%' or name like '%${queryString}%' or helpCode like '%${queryString}%'"
            + "</if>"
            + "</script>")
    Page<CheckGroup> findPage(@Param("queryString") String queryString);

    //根据id查询检查组信息
    @Select("select * from t_checkgroup where id=#{id}")
    public CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询中间表对应的检查项id
     * @param checkgroup_id
     * @return
     */
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{checkgroup_id}")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroup_id);

    //更新t_checkgroup检查组表
    @Update("update t_checkgroup set name = #{name},sex = #{sex},code = #{code}, helpCode = #{helpCode},remark = #{remark} where id=#{id}")
    public void edit(CheckGroup checkGroup);
    //根据检查组id删除中间表对应的检查项的id
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id=#{id}")
    public void deleteAssociation(Integer id);


    //删除检查组
    @Delete("delete from t_checkgroup  where id=#{id}")
    public void deteleById(Integer id);

    @Select("select * from t_checkgroup")
    List<CheckGroup> findALl();

    /**
     * 根据套餐id查询检查组数据
     */
    @Select("select * from t_checkgroup where id in(select checkgroup_id from t_setmeal_checkgroup  where setmeal_id=#{id})")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "code",column = "code"),
            @Result(property = "name",column = "name"),
            @Result(property = "helpCode",column = "helpCode"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "remark",column = "remark"),
            @Result(property = "attention",column = "attention"),
            //多对多查询 检查组<---->检查项
            @Result(property = "checkItems",column = "id",javaType = List.class,many = @Many(select = "com.itheima.dao.CheckItemDao.findByCheckGroupId")),


    })
    public List<CheckGroup> findBySetmealId(Integer id);
}
