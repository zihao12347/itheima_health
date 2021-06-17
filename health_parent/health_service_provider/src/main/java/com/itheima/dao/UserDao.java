package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * 用户dao层
 */
public interface UserDao {
    /**
     * 根据用户名查询用户信息，其中查询结果包括：用户信息，用户对应的角色信息，用户角色对应的权限信息
     *          用户 <---->角色     多对多关系
     *          角色 <---->权限     多对多关系
     *
     * @param username
     * @return
     */

    @Select("select * from t_user where username=#{username}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "roles",column = "id",javaType = java.util.Set.class,many = @Many(select = "com.itheima.dao.RoleDao.findByUserId"))
    })
    public User findByUsername(String username);
}
