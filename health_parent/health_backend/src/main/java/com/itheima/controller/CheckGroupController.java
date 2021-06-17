package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import com.itheima.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference  //获取zk中的服务
    private CheckGroupService checkGroupService;


    /**
     * 新增检查组方法
     *
     * @param checkGroup   新增检查组的基本信息，
     * @param checkitemIds 检查组和检查项进行关联需要的id集合
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据检id查询检查组信息
     * @param id
     * @return
     */

    @GetMapping("/findById")
    public Result findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        //判断查询结果是否为空
        if (checkGroup != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }
    /**
     * 根据检查组id查询中间表对应的检查项id
     * @param id
     * @return
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){

        try {
            List<Integer> ids = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,ids);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }

    /**
     * 根据传递的检查组信息，和检查项ids完成更新操作：
     *      1.更新检查组和检查项的中间表：先删除后添加
     *                      思路：先根据检查组的ids删除对应的检查项id，
     *                           再根据传递的检查项ids重新添加
     *      2.更新检查组的表
     * @param checkGroup
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    @PostMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] ids){
        try {
            checkGroupService.edit(checkGroup,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_FAIL);
    }

    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @DeleteMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            checkGroupService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有的检查组信息
    @GetMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
