package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class orderSettingController {
    @Reference  //查找服务
    private OrderSettingService orderSettingService;

    /**
     * 批量导入预约设置信息
     * 前台：
     * 1.用户点击模板下载按钮，将excel模板文件下载，
     * 2.用户下载好excel文件后，设置预约信息
     * 3.用户设置好预约信息后，点击上传文件按钮，发送到当前控制类的文件上传方法中
     * 后台：
     * 1.调用POIUtils工具类的文件读取方法，读取上传的excel文件数据
     * 2.并将读取到的excel文件内容，保存在数据中
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public Result excelupload(@RequestParam("excelFile") MultipartFile multipartFile) throws IOException {
        try {
            //1.调用POIUtils工具类的文件读取方法，获取上传的excel文件数据
            List<String[]> readExcel = POIUtils.readExcel(multipartFile);
            //判断readexcel集合是否为空
            if (readExcel != null && readExcel.size() > 0) {
                //创建一个OrderSetting类型的list集合，用于保存excel读取的数据,以便完成将数据保存到数据库中
                List<OrderSetting> list = new ArrayList<>();
                //遍历readexcel集合
                for (String[] strings : readExcel) {
                    //创建OrderSetting对象,保存excel集合中每个String数组数据（也就是保存excel中的每行数据，用于后面进行保存在数据库中）
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    //将orderSetting对象封装到list集合中，实现批量保存在数据库中
                    list.add(orderSetting);
                }
                list.forEach(l-> System.out.println(l.getOrderDate()+""+l.getNumber()));
                //远程调用service服务完成将excel中的数据保存在数据库中
                orderSettingService.add(list);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> list = this.orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据日期设置某天可预约的人数
     * @param orderSetting
     * @return
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            this.orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
