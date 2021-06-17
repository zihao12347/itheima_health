package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * report运营统计服务
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {


    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 运营数据统计
     *           reportData:{    //数据格式
     *                     reportDate:null,
     *                     todayNewMember :0,
     *                     totalMember :0,
     *                     thisWeekNewMember :0,
     *                     thisMonthNewMember :0,
     *                     todayOrderNumber :0,
     *                     todayVisitsNumber :0,
     *                     thisWeekOrderNumber :0,
     *                     thisWeekVisitsNumber :0,
     *                     thisMonthOrderNumber :0,
     *                     thisMonthVisitsNumber :0,
     *                     hotSetmeal :[
     *                         {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
     *                         {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
     *                     ]
     *                 }
     *
     * @return
     */
    @Override
    public Map<String, Object> getBusineseReportData() throws Exception {
        HashMap<String, Object> map = new HashMap<>();

        String today = DateUtils.parseDate2String(DateUtils.getToday());//获取当前日期
        String week = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//获取周一的日期
        String month = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//获取本月的第一天
        map.put("reportDate",today );//为reportDate赋值，设置当前日期时间
        //会员数量统计
        map.put("todayNewMember",memberDao.findMemberCountByDate(today));
        map.put("totalMember", memberDao.findMemberTotalCount());
        map.put("thisWeekNewMember",memberDao.findMemberCountByDate(week));
        map.put("thisMonthNewMember",memberDao.findMemberCountByDate(month));

        //会员订单统计
        map.put("todayOrderNumber",orderDao.findOrderCountByDate(today));
        map.put("thisWeekOrderNumber",orderDao.findOrderCountAfterDate(week));
        map.put("thisMonthOrderNumber",orderDao.findOrderCountAfterDate(month));

        //会员到诊统计
        map.put("todayVisitsNumber",orderDao.findVisitsCountByDate(today));
        map.put("thisWeekVisitsNumber",orderDao.findVisitsCountAfterDate(week));
        map.put("thisMonthVisitsNumber",orderDao.findVisitsCountAfterDate(month));

        //热门套餐，根据套餐名称分组，查询每个套餐的预约数据，并计算出占比数据
        List<Map<String, Object>> hotSetmeal = this.orderDao.findHotSetmeal();
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
