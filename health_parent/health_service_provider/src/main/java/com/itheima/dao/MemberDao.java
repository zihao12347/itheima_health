package com.itheima.dao;

import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.Date;

/**
 * 会员dao接口
 */
public interface MemberDao {
    //根据电话号码查询当前用户是否为会员
    @Select("select * from t_member where phoneNumber =#{phonenumber}")
    public Member findByPhonenumber(String phonenumber);

    /**
     * 创建会员
     * @param member
     */
    @Insert("insert into t_member (name,sex,idCard,phoneNumber,regTime)values(#{name},#{sex},#{idCard},#{phoneNumber},#{regTime})")
    @SelectKey(statement = {"select last_insert_id()"},keyColumn = "id",keyProperty ="id" ,before = false,resultType = Integer.class)
    public void addMember(Member member);
    /**
     * 根据月份进行查询数据库的会员信息
     */
    @Select("SELECT COUNT(id) from t_member WHERE regTime <=#{date}")
    public Integer findMemberCountBeforeDate(String date);

    @Select("select count(id) from t_member where regTime<=#{date}")
    public Integer findMemberCountByDate(String date);
    @Select("select count(id) from t_member")
    public Integer findMemberTotalCount();
}
