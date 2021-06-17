package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查组服务层接口
 */
public interface CheckGroupService {

    //新增检查组
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);
    //分页查询方法
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);
    /**
     * 根据检查组id查询中间表对应的检查项id
     * @param checkgroup_id
     * @return
     */

    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroup_id);

    //更新操作
    public void edit(CheckGroup checkGroup, Integer[] ids);

    //删除检查组
    void deleteById(Integer id);

    List<CheckGroup>  findAll();
}
