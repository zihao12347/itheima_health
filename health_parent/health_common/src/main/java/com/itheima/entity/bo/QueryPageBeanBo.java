package com.itheima.entity.bo;

import com.itheima.entity.QueryPageBean;

import java.util.Arrays;

/**
 * QueryPageBean业务对象，
 *      继承QueryPageBean,就可以拥有QueryPageBean的所有属性，来完成具体的业务功能
 */
public class QueryPageBeanBo extends QueryPageBean {
    private String selectedDate;   //日期查询条件
    private String startDate;   //开始日期
    private String endDate;   //开始日期
    private String orderType;   //预约类型
    private String orderStatus; //预约状态
    private String nameOrphoneNumber;   //会员姓名或手机号

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNameOrphoneNumber() {
        return nameOrphoneNumber;
    }

    public void setNameOrphoneNumber(String nameOrphoneNumber) {
        this.nameOrphoneNumber = nameOrphoneNumber;
    }

    @Override
    public String toString() {
        return "QueryPageBeanBo{" +
                "selectedDate='" + selectedDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", nameOrphoneNumber='" + nameOrphoneNumber + '\'' +
                '}';
    }
}
