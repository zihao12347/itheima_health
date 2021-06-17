package com.heima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.ValidateCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 用户会员控制类
 */
@RestController
@RequestMapping("/login")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    /**
     * 用户会员登录方法
     * @param map
     * @param response
     * @return
     */
    @RequestMapping("/check")
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //1.获取前台体检的电话号码，验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String RedisValidCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        //2.获取Redis中保存的验证码，并与前台体检的验证码进行比对
        if (RedisValidCode==null||!RedisValidCode.equals(validateCode)) {//当验证码错误，设置错误提示
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {//当验证码正确，
            //3.根据电话号码判断用户是否为会员
             Member member = this.memberService.findByPhoneNumber(telephone);
            if (member == null) {//当用户不是会员时候，完成自动注册
                member=new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                this.memberService.addMember(member);
            }
            //4.1向客户端写入Cookie，内容为用户手机号追踪用户，记住用户登录的状态，方便过滤用户登录后才能执行某操作,进行跟踪用户区分用户
            Cookie cookie = new Cookie("telephone", telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(30*24*60*60);//有效30天
            //4.2将会员信息到redis中，使用手机号作为key,保存时长为30分钟
            /**
             *   session：服务器会话技术
             *   cookie：客户端会话技术
             *
             *
             * 当前采用将会员信息保存到redis中，而不保存在服务器的session中的原因是，
             *      当后期移动端部署了集群服务的话，那么session共享就是麻烦
             *   当前采用redis保存会员信息，目的是方便后续，过滤拦截用户是否认证完成，然后进行授权
             *
             *      当前redis中保存的是member对象数据，是否为了后续查询会员信息
             *
             */
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60*30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);//完成登录，设置登录成功信息
        }
    }
}
