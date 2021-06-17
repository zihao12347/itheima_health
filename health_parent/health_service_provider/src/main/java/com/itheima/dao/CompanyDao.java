package com.itheima.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.Result;
import com.itheima.pojo.Company;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CompanyDao {
    @Insert("INSERT INTO t_company (address,longtitude,latitude) VALUES(#{address},#{longtitude},#{latitude})")
    public void addCompany(Company company);

    @Select("<script>"
            + "select * from t_company where 1=1"
            + "<if test='queryString != null and queryString.length > 0'>"
            + "and address like '%${queryString}%' "
            + "</if>"
            + "</script>")
    public Page<Company> findByCondition(@Param("queryString") String queryString);


    @Select("select address, longtitude,latitude from t_company  ")
    public List<Map<String,String>> findAll();

    @Delete("delete from t_company where id=#{id}")
    public void delete(Integer id);
}
