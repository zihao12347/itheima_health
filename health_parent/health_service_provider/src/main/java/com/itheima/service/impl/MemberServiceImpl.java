package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户会员service
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    /**
     * 根据电话号码查询用户是否为会员
     * @param PhoneNumber
     * @return
     */
    @Override
    public Member findByPhoneNumber(String PhoneNumber) {
        Member member = this.memberDao.findByPhonenumber(PhoneNumber);
        return member;
    }

    /**
     * 完成用户注册会员
     * @param member
     */
    @Override
    public void addMember(Member member) {
        //当密码不为空时，使用md5算法处理用户设置的明文密码为加密后的密码保存到数据库中
        String password = member.getPassword();
        if (password != null) {
            member.setPassword(MD5Utils.md5(password));
        }
        this.memberDao.addMember(member);
    }

    /**
     * 根据月份统计会员数量
     * @param month
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> month) {
        List<Integer> list = new ArrayList<>();//创建list集合，保存查询的结果
        for (String m : month) {//1.遍历传递过来的12个月份。进行处理，并且查询数据库
            //2.再为传递过来的12个月份添加上具体的日期，每个月最后一个天31
            m = m + ".31";
            Integer countBeforeDate = this.memberDao.findMemberCountBeforeDate(m);
            list.add(countBeforeDate);
        }
        return list;
    }


}
