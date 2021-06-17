package com.heima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SmsUtil;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 移动端发送验证码控制器类
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    //注入jedis连接池对象
    @Autowired
    private JedisPool jedisPool;
    /**
     * 发送6位数随机数验证码
     * @param phonenumber
     * @return
     */
    @PostMapping("/send6Order")
    public Result send6Order(String phonenumber){
        //调用ValidateCodeUtils工具类方法生成6位数的随机数验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);

        try {
            //调用SMSUtils工具类方法发送验证码
            SmsUtil.sendCode(phonenumber,validateCode.toString() );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        /**
         * 将生成的随机数验证码报错到redis中，方便判断输入的验证码是发是有效时间内的验证码
         *      Redis字符串
         *          setex(key ,seconds,value)方法，指定key值为value,secondsg过期时间 5分钟有效期，
         *              如果key的值以及存在，setex命令将会替换旧值
         *
         *
         */
       jedisPool.getResource().setex(phonenumber+RedisMessageConstant.SENDTYPE_ORDER, 5*60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    @PostMapping("/send6Login")
    public Result send6Login(String phonenumber){
        //调用ValidateCodeUtils工具类方法生成6位数的随机数验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);

        try {
            //调用SMSUtils工具类方法发送验证码
            SmsUtil.sendCode(phonenumber,validateCode.toString() );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        /**
         * 将生成的随机数验证码报错到redis中，方便判断输入的验证码是发是有效时间内的验证码
         *      Redis字符串
         *          setex(key ,seconds,value)方法，指定key值为value,secondsg过期时间 5分钟有效期，
         *              如果key的值以及存在，setex命令将会替换旧值
         *
         *
         */
        jedisPool.getResource().setex(phonenumber+ RedisMessageConstant.SENDTYPE_LOGIN, 3*60*60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
