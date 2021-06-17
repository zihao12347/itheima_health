package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.entity.bo.QueryPageBeanBo;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.BackAppointmentService;
import com.itheima.service.OrderService;
import com.itheima.service.SetMealService;
import com.sun.tools.corba.se.idl.ParameterGen;
import org.springframework.core.codec.EncodingException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 后台预约列表
 */
@RequestMapping("/BackAppointment")
@RestController
public class BackAppointmentController {
    @Reference
    private BackAppointmentService backAppointmentService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private OrderService orderService;

    /**
     * 预约列表分页查询
     *
     * @param queryPageBeanBo
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findByPage(@RequestBody QueryPageBeanBo queryPageBeanBo) throws Exception {
        PageResult pageResult = this.backAppointmentService.findByPage(queryPageBeanBo);
        return pageResult;
    }

    @GetMapping("/updateOrderStatus")
    public Result updateOrderStatus(Integer id,Boolean orderStatus) throws Exception {
            String  orderStatus_=new String();
        if (orderStatus) {
            orderStatus_="已到诊";
        }else {
            orderStatus_="未到诊";
        }
        try {
            this.orderService.updateOrderStatus(id,orderStatus_);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.Update_ORDERStatus_FAIL );
        }
        return new Result(true, MessageConstant.Update_ORDERStatus_SUCCESS);
    }


    /**
     * pc端完成为用户设置体检预约
     *
     * @param map
     * @param setmeal_ids
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Map map, Integer[] setmeal_ids) throws Exception {
        map.put("setmeal_ids", setmeal_ids);
        map.put("orderType", Order.ORDERTYPE_TELEPHONE);

         Result result = this.orderService.order(map);
            return result;
    }

    /**
     * 删除套餐信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            this.backAppointmentService.delete(id);
            return new Result(true, MessageConstant.Delete_ORDER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.Delete_ORDER_FAIL);
        }
    }

    /**
     * 查询所有套餐列表
     *
     * @return
     */
    @RequestMapping("/findAllSetmeal")
    public Result findAllSetmeal() {
        try {
            List<Setmeal> setmealList = this.setMealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_FAIL, setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }
}
