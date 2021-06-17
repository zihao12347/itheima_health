package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    public Member findByPhoneNumber(String PhoneNumber);
    public void addMember(Member member);
    //获取过去一年时间内每个月的会员数据量
    public List<Integer> findMemberCountByMonth(List<String> month);

}
