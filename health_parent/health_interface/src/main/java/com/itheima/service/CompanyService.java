package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Company;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    public void addcompany(Company company);
    public PageResult findByPage(QueryPageBean queryPageBean);
    List<Map<String,String>>  findAll();
    public void delete(Integer id);
}
