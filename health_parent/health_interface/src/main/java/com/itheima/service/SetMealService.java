package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SetMealService {
    //新增套餐
    public void add(Setmeal setmeal, Integer[] ids) throws IOException, TemplateException;
    //分页查询，
    public PageResult findPage(QueryPageBean queryPageBean);
    //获取套餐列表数据
    public List<Setmeal> findAll();
    //根据套餐id获取套餐详情数据
    public Setmeal findMealDeatailById(Integer id);
    public Setmeal findById(Integer id);
    public List<Map<String,Object>> findSetmealCount();
}
