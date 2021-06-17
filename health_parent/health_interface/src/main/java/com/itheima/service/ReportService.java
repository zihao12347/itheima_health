package com.itheima.service;

import java.util.Map;

/**
 * 运营数据统计service接口
 */
public interface ReportService {
    //运营数据统计
    public Map<String,Object> getBusineseReportData() throws Exception;
}
