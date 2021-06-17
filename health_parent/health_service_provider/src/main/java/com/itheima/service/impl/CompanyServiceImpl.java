package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CompanyDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Company;
import com.itheima.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = CompanyService.class)
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    /**
     * 新增公司地址
     * @param company
     */
    @Override
    public void addcompany( Company company) {
        System.out.println(company.getAddress());
        this.companyDao.addCompany(company);
    }

    /**
     * 分页查询公司地址
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
        Page<Company> Page = this.companyDao.findByCondition(queryString);
        long total = Page.getTotal();
        List<Company> list = Page.getResult();
        return new PageResult(total, list);
    }

    @Override
    public List<Map<String,String>> findAll() {
        List<Map<String,String>>   Lmap = this.companyDao.findAll();
        return Lmap;
    }

    @Override
    public void delete(Integer id) {
        this.companyDao.delete(id);
    }
}
