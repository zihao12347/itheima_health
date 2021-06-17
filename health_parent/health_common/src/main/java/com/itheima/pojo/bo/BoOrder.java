package com.itheima.pojo.bo;

import com.itheima.pojo.Order;

import java.util.Date;

/**
 * Order实体类的业务对象
 */
public class BoOrder extends Order {
    private String name;    //会员名称
    private String phoneNumber;     //电话号码
    private String sname;       //套餐名称
    private String date;//预约日期

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BoOrder{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", sname='" + sname + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
