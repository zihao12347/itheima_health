package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;


import com.itheima.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Reference  //获取服务
    private SetMealService setMealService;
    @Reference
    private UploadService uploadService;

    //注入JedisPool连接池bean对象完成操作redis服务，
    @Autowired
    private JedisPool jedisPool;
    //新增套餐
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @PostMapping("/add")
    public  Result add(@RequestBody Setmeal setmeal, Integer[] ids) {
        try {
            setMealService.add(setmeal, ids);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 1.文件上传：
     *      文件上传思路：
     *          当前的采取的文件上传方式是采用第三方云存储，七牛云，
     *          1.1.前台页面采用el-upload组件来实现文件上传，name属性指定上传文件的名称，
     *              action属性：指定文件上传的的地址，/setmeal/upload,前台将完成上传到当前的controller控制器中
     *          1.2.在控制器类中采用QiniuUitl工具类来实现文件真正上传到七牛云服务器中
     * @param multipartFile
     *  2.完善文件上传：
     *      当用户在新增套餐弹层中向七牛云存储空间上传了图片后，
     *      但是并没有点击确认新增按钮，保存套餐信息到数据库中，
     *      此时上传到七牛云的图片则为垃圾图片，对于这些垃圾图片我们需要定时清理来释放磁盘
     *      2.1如何确定哪些图片是垃圾图片：   方案：利用redis来缓存图片名称
     *          1)当用户上传图片后，将图片名称保存至redis都set集合中
     *          2)当用户提交添加套餐后，将图片名称保存redis的另一个set集合中
     *          3)计算setMealPicResources集合与setMealPicDbResources集合的差值，
     *                  差值结果就是这些垃圾图片的集合，只要定时清理这些垃圾图片即可
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile) throws Exception {
        //获取原始文件名称的后缀名称
        String originalname = multipartFile.getOriginalFilename();
        //获取原始文件的最后.的位置，目的是为了截取原始名称的文件格式：aaa.jpg
        //StringUtils字符串工具类，subStringAfterLast(str,separator)方法，从分割符出现最后一次位置向后截取
        //String extention = StringUtils.substringAfterLast(originalname, ".");
        int lastIndexOf = originalname.lastIndexOf(".");
        String extention = originalname.substring(lastIndexOf);
        //1.使用uuid产生一个随机的文件名称+原始文件的后缀格式,防止文件上传得多文件名称同名覆盖
        String filename = UUID.randomUUID().toString()+extention;
        /**
         * 使用QiniuUtils工具类的upload方法实现上传到七牛云存储空间中
         * QiniuUtils.upload2Qiniu(multipartFile.getBytes(), filename);
         */
        String uploadfileaddress = this.uploadService.upload(multipartFile.getBytes(),extention);
        //完善文件上传1.将filename上传的图片名称保存在redis的setMealPicResources集合中
        //jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, uploadfileaddress);

        return new Result(true, MessageConstant.UPLOAD_SUCCESS,uploadfileaddress);
    }
    //分页查询方法
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            PageResult pageResult = setMealService.findPage(queryPageBean);
            return new PageResult(pageResult.getTotal(),pageResult.getRows());
    }

}
