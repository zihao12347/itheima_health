package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)  //发布服务
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组方法1.
     *
     * @param checkGroup   checkGroup参数：向t_checkgroup表中添加数据
     * @param checkitemIds checkitemIds参数：向t_checkgroup_checkitem（t_checkitem和t_checkgroup的中间表）添加数据，
     *                     也就是设置检查组和检查项之间的关联关系
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        //调用新增检查组方法2，传递检查组的id和新增时选中的检查项列表的id.设置检查组和检查项之间的关联关系
        Integer id = checkGroup.getId();
        setCheckGroupAndCheckItem(id, checkitemIds);
    }


    /**
     * 新增检查组方法2.
     * 设置检查组和检查项之间的关联关系
     *
     * @param checkGroupId  检查组的id
     * @param checkItemsIds 检查项的id
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemsIds) {
        //当检查项的id大于0，表示检查项能过与检查组进行关联
        if (checkItemsIds != null && checkItemsIds.length > 0) {
            //map变量最好声明在循环外面，有助于内存的节约
            Map<String, Integer> map = null;
            //遍历检查项的id
            for (Integer checkItemsId : checkItemsIds) {
                //创建map对象，以key-value的形式来封装checkGroupId和checkItemsIds的数据
                //Map<String,Integer> map=new HashMap<>();
                map = new HashMap<>();
                //保存checkGroupId
                map.put("checkgroup_id", checkGroupId);
                //保存checkItemsIds
                map.put("checkitem_id", checkItemsId);
                //调用dao的setCheckGroupAndCheckItem方法完成，设置检查组和检查项之间的关联关系
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }


    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用mybatis提供的startPage()方法进行生成分页sql语句
        PageHelper.startPage(currentPage,pageSize);
        //调用dao中的分页方法
        Page<CheckGroup> checkGroups = checkGroupDao.findPage(queryString);
        //每页展示的数据集合
        List<CheckGroup> groupList = checkGroups.getResult();
        //总记录数
        long total = checkGroups.getTotal();
        return new PageResult(total, groupList);
    }

    /**
     * 更新检查组信息，同时需要重新关联检查项
     *          1.更新检查组和检查项的中间表：先删除后添加
     *              思路：先根据检查组的ids删除对应的检查项id，
     *                  再根据传递的检查项ids重新添加
     *           2.更新检查组的表
     * @param checkGroup
     * @param ids
     */
    @Override
    public void edit(CheckGroup checkGroup,Integer[] ids) {
        //1.完成检查组和检查项中表表的更新操作:先删除再添加
        //1.1先根据检查组的id删除中间表与对应的检查项id
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //1.2重新添加中间表检查组id与检查项id的对应关系
        setCheckGroupAndCheckItem(checkGroup.getId(),ids);
        //2.完成检查组更新操作
        checkGroupDao.edit(checkGroup);
    }

    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroup_id){
        List<Integer> ids = checkGroupDao.findCheckItemIdsByCheckGroupId(checkgroup_id);
        return ids;
    }
    //根据id删除检查组
    @Override
    public void deleteById(Integer id) {
        //1.根据检查组的id删除中间表的检查组与检查项对应关系
        checkGroupDao.deleteAssociation(id);
        //2.根据检查组id删除对应的检查组信息
        checkGroupDao.deteleById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> checkGroups = checkGroupDao.findALl();
        return checkGroups;
    }
}
