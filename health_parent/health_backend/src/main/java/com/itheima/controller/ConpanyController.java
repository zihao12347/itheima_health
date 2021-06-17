package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Company;
import com.itheima.service.CompanyService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 公司分布情况
 */
@RestController
@RequestMapping("/company")
public class ConpanyController {
    @Reference
    private CompanyService companyService;

    /**
     * 在地图上显示已添加的公司地址位置
     *
     * @return
     */
    @RequestMapping("/findall")
    public Result findall() {
        try {
            List<Map<String, String>> map = this.companyService.findAll();
            return new Result(true, MessageConstant.ADD_COMPANY_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_COMPANY_FAIL);
        }
    }


    /**
     * 新增公司地址
     *
     * @param company
     * @return
     */
    @RequestMapping("/add")
    public Result addCompany(@RequestBody Company company) {
        try {
            this.companyService.addcompany(company);
            return new Result(true, MessageConstant.ADD_COMPANY_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_COMPANY_FAIL);
        }
    }

    /**
     * 分页查询公司地址信息
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findpage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = this.companyService.findByPage(queryPageBean);
        return new PageResult(pageResult.getTotal(), pageResult.getRows());
    }


    /**
     * 删除公司地址信息
     *
     * @param
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            this.companyService.delete(id);
            return new Result(true, MessageConstant.DELETE_COMPANY_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_COMPANY_FAIL);
        }
    }
}
