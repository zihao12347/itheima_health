package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约service
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 移动端体检预约设置
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result order(Map map) throws Exception {
        //1.查询判断用户预约的日期是否已经进行了预约设置
        String date = (String) map.get("orderDate");
        Date orderDate = DateUtils.parseString2Date(date);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {//当orderSetting为空时表示，没有提前进行预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.判断用户预约的日期可预约名额是否已经已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {//当已预约人数大于可预约人数表示预约已满不可预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.根据手机号判断用户是否是会员，当用户不是会员时，先完成会员的注册，当用户是会员时，直接完成预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByPhonenumber(telephone);
        if (member != null) {//当是会员时，
            //4.查询判断用户是否重复预约(同一个用户同一个日期预约同于的套餐项目)
            String setmealId = (String) map.get("setmealId");
            List<Order> list = orderDao.findByCondition(member.getId(),orderDate,Integer.parseInt(setmealId));
            if (!CollectionUtils.isEmpty(list)){//当list不为空表示用户当前日期已经预约了当前选择的套餐
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }else {
            member=new Member();
            //当当前用户不是会员时，先完成注册会员
            member.setIdCard((String) map.get("idCard"));
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.addMember(member);//完成会员注册
        }

        //5.完成预约设置，并更新套餐设置：已预约人数+1
        Integer[] setmeal_ids =(Integer[]) map.get("setmeal_ids");
        Order order=null;
        if (setmeal_ids != null && setmeal_ids.length > 0) {
            for (int i = 0; i <setmeal_ids.length ; i++) {
                order=new Order(member.getId(),orderDate,(String)map.get("orderType"),Order.ORDERSTATUS_NO,setmeal_ids[i]);
                orderDao.add(order);//完成预约设置
                orderSetting.setReservations(reservations+1);
                orderSettingDao.editReservations(orderSetting);//更新已预约人数
            }
        }
        order=new Order(member.getId(),orderDate,(String)map.get("orderType"),Order.ORDERSTATUS_NO,(Integer) map.get("setmeal_id"));
        orderDao.add(order);//完成预约设置
        orderSetting.setReservations(reservations+1);
        orderSettingDao.editReservations(orderSetting);//更新已预约人数


        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS,order.getId());//返回orderId给前台，完成成功续约信息回显
    }

    @Override
    public void updateOrderStatus(Integer id, String orderStatus) {
        this.orderDao.updateOrderState(id,orderStatus);
    }

    /**
     * 根据orderId预约id查询成功预约数据回显
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = this.orderDao.findById(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
