package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 检查项的dao接口
 */
public interface CheckItemDao {
    /**
     * 查询检查项所有方法
     *
     * @return
     */
    @Select("select * from t_checkitem")
    public List<CheckItem> findAll();

    @Insert(" insert into t_checkitem(code,name,sex,age,price,type,remark,attention)" +
            "                      values" +
            "        (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    public void add(CheckItem checkItem);

    /**
     * 检查项的分页查询：
     * 1.使用mybatis的@Select注解的实现动态sql语句写法：
     * 在@Select()注解中使用"<script><script/>"标签包围住，然后像写xml语法一样书写
     *
     * @param queryString
     * @return
     * @Select("<script>" ----<script>开始包围
     * +"select * from mi_taobao where 1=1"     ----sql模板
     * +"<if test='status != null'>"    ----if条件判断，判断当test为true时表示status不为空，拼装动态sql
     * +"and status = #{status}"
     * +"</if>"
     * +"</script>")
     * 2.Page<pojo实体类>是mybatis中的PageHelper插件提供的对象，用于封装查询的返回值
     */
    @Select("<script>"
            + "select * from t_checkitem where 1=1"
            + "<if test='queryString != null and queryString.length > 0'>"
            + "and code like '%${queryString}%' or name like '%${queryString}%'"
            + "</if>"
            + "</script>")
    public Page<CheckItem> selectByCondition(@Param("queryString") String queryString);

    /**
     * 根据id删除检查项
     *
     * @param id
     */
    @Delete("delete from t_checkitem where id=#{id}")
    public void deleteById(Integer id);

    /**
     * 根据检查项id查询中间关系表:
     * 由于检查项和检查组是多对多的关系，
     * 如果检查项与检测组进行关联，则不允许删除改检查项
     */

    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id=#{checkItemId}")
    public long findCountByCheckItemId(Integer checkItemId);


    /**
     * 根据id查询检查项
     *
     * @param id
     * @return
     */
    @Select("select * from t_checkitem where id=#{id}")
    public CheckItem findById(Integer id);

    @Update("update t_checkitem set  name = #{name},sex = #{sex},code = #{code},age = #{age},price = #{price},type = #{type},attention = #{attention}, remark = #{remark} where id=#{id}")
    public void edit(CheckItem checkItem);
    /**
     * 根据检查组id查询检查项数据
     */
    @Select("select * from t_checkitem where id in(select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id})")
    public List<CheckItem> findByCheckGroupId(Integer id);
}
