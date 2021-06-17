package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.Set;

/**
 * 角色dao层
 */
public interface RoleDao {
    @Select("select * from t_role where id in(select role_id from t_user_role where user_id=#{id})")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "keyword",column = "keyword"),
            @Result(property = "permissions",column = "id",many = @Many(select = "com.itheima.dao.PermissionDao.findByRoleId")),
    })
    public Set<Role> findByUserId(Integer id);
}
