package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项服务层接口
 */
public interface CheckItemService {

    //查询所有的检查项
    public List<CheckItem> findAll();

    /**
     * 新增检查项方法
     * @param checkItem
     */
     void add(CheckItem checkItem);

    /**
     * 检查项分页查询方法
     * @param queryPageBean
     * @return
     */

     PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 根据id删除检查项
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id删除检查项
     * @param id
     */
    CheckItem findById(Integer id);

    /**
     * 编辑检查项目
     * @param checkItem
     * @return
     */
    void edit(CheckItem checkItem);
}
