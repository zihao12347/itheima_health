package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.bo.QueryPageBeanBo;

import java.util.Map;

public interface BackAppointmentService {
    public PageResult findByPage(QueryPageBeanBo queryPageBeanBo) throws Exception;
    public void delete(Integer id);
    public void add(Map map, Integer[] setmeal_ids);
}
