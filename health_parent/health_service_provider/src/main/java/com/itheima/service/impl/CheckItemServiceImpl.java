package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    //注入checkItemDao对象
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> checkItemList = checkItemDao.findAll();
        return checkItemList;
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();    //分页查询的条件
        //使用mybatis的分页查询插件PageHelper对象来完成分页查询
        //调用PageHelper对象的startpage(PageNum,PageSize)方法进行生成分页的sql语句
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();//获取总记录数
        List<CheckItem> result = page.getResult();  //获取每页显示的数据集合
        PageResult pageResult=new PageResult(total, result);
        return pageResult;
    }

    /**
     * 根据id删除检查项
     *      注意：不能根据id直接删除检查项，由于检查项与检查组进行了关联，是多对多的关系，
     *              如果检查项与检查组已经进行关联了则不能被删除，
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //查询当前检查项的id是否与检查组的id进行了关联，如查询结果大于0则表示关联了，不能被删除
        long countByCheckItemId = checkItemDao.findCountByCheckItemId(id);
        if (countByCheckItemId>0){  //已经进行了关联，自定义一个异常
            throw new RuntimeException("当前检查项被引用，不能删除！");
        }
        checkItemDao.deleteById(id);

    }

    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }


}
