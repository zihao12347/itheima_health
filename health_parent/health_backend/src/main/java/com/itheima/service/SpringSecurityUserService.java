package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  由于当前处理登录退出操作使用的是springsecurity安全认证框架服务架构
 *  所以处理login.html登录对象不是springmvc的控制器，而是交给spring-security框架完成处理，
 *  因此spring-security不知道该调用哪个service来完成从数据库中查询操作
 *  1.自定义service可以继承UserDetailsService来标识spring-security需要调用哪个service
 *  2.自定义service需要实现UserDetailsService的loadUserByUsername(loadUserByUsername)方法来完成操作
 *
 *
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    //使用dubbo通过网络远程调用服务提供方获取数据库中的用户信息
    @Reference
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * 根据用户名查询用户详情信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user==null){//用户不存在时
            return null;
        }
        //动态为当前用户授权
        List<GrantedAuthority> list=new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (roles!=null){
            for (Role role : roles) {//遍历角色集合，为当前用户授予角色
                list.add(new SimpleGrantedAuthority(role.getKeyword()));//获取role中的描述角色的关键字，为当前用户授予角色
                //遍历权限，为用户授予权限
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null) {
                    for (Permission permission : permissions) {//遍历权限集合，为当前用户授予权限
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));//获取permission中描述权限的关键字，为当前用户授予权限
                    }
                }
            }
        }
        //创建spring-security框架的user对象，再返回给框架
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return securityUser;
    }
}
