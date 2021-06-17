package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义job类，实现定时清理垃圾图片
 */
public class ClearImagJob {
    //注入JedisPool连接池对象
    @Autowired
    private JedisPool jedisPool;
    //定时清除垃圾图片方法
    public void clearImg(){
        //sdiff()返回一个集合与给定集合的差集元素
        //1.使用Redis都sdiff()方法进行对redis保存的两个集合进行差值计算，结果为set集合

        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //当差值结果集合不为空时，
        if (sdiff!=null&&sdiff.size()>0){
            //2.遍历集合元素
            for (String s : sdiff) {
                //3.删除上传到七牛云上的垃圾图片

                QiniuUtils.deleteFileFromQiniu(s);
                //4.删除保存在redis中的垃圾图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, s);
            }
        }
    }
}
