package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.BackAppointmentDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.bo.QueryPageBeanBo;
import com.itheima.pojo.bo.BoOrder;
import com.itheima.service.BackAppointmentService;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = BackAppointmentService.class)
@Transactional
public class BackAppointmentServiceImpl implements BackAppointmentService {
    @Autowired
    private BackAppointmentDao backAppointmentDao;
    @Override
    public PageResult findByPage(QueryPageBeanBo queryPageBeanBo) throws Exception {
        String selectedDate = queryPageBeanBo.getSelectedDate();//查询条件：选中日期，String类型封装一个json数组:[
        if (!StringUtils.isEmpty(selectedDate)) {//当selectedDate不为空，解析selectedDate数据：
            String s1 = selectedDate.replace("[", "");//"2021-06-01", "2021-07-07"]
            String s2 = s1.replace("]", "");//"2021-06-01", "2021-07-07"
            //使用\"转义符号将前后的"abc"双引号转义成abc字符串；
            String s3 = s2.replace("\"", "");
            String[] split = s3.split(",");
            queryPageBeanBo.setStartDate( split[0]);
            queryPageBeanBo.setEndDate(split[1]);

        }
        PageHelper.startPage(queryPageBeanBo.getCurrentPage(),queryPageBeanBo.getPageSize());
        Page<BoOrder> page = this.backAppointmentDao.findByCondition(
                queryPageBeanBo.getStartDate(),
                queryPageBeanBo.getEndDate(), queryPageBeanBo.getOrderType(), queryPageBeanBo.getOrderStatus(), queryPageBeanBo.getNameOrphoneNumber());
        long total = page.getTotal();
        List<BoOrder> boOrders = page.getResult();

        return new PageResult(total, boOrders);
    }

    @Override
    public void delete(Integer id) {
        this.backAppointmentDao.delete(id);
    }

    /**
     * pc端完成为用户设置体检预约
     * @param map
     * @param setmeal_ids
     */
    @Override
    public void add(Map map, Integer[] setmeal_ids) {
        //1.完成体检预约设置
    }
}
