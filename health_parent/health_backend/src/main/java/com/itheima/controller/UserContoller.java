package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息控制类
 */
@RestController
@RequestMapping("/user")
public class UserContoller {
    /**
     * 获取已登录的用户信息，
     *  Spring-Security框架，在用户认证成功后，会将用户信息保存到框架提供的上下文对象中  底层基于session会话技术
     * @return
     */
    @RequestMapping("/getusername")
    public Result getUsername(){


        try {
            //获取spring-security框架提供的上下文对象，转化成user类

            User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user!=null){
                return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
