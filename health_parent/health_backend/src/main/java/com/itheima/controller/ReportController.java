package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetMealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表操作控制器
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService mealService;


    /**
     * 会员数量统计:  获取过去一年时间内的每个月的会员的总数量
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        //1.计算过去一年的12个月
        Calendar calendar = Calendar.getInstance();//实例化calendar对象，获取当前时间
        /**Calendar日期时间对象：定义了一些处理日期时间的方法，通常用来操作时期时间
         *      calendar对象的add(int field,int amount)根据日历规则，为给定的日期的字段field添加或者减去指定的时间量amount
         */
        calendar.add(Calendar.MONTH, -12);//获取当前月份的前12个月时间,也就是减去12个月

        //2创建list集合，并保存遍历的过去12个月份
        List<String> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");
        for (int i = 0; i < 12; i++) {//遍历12个月
            calendar.add(Calendar.MONTH, 1);//每遍历一次+1个月，也就是从前12个月分别进行往后推一个月
            list.add(simpleDateFormat.format(calendar.getTime()));//并且保存这12个月
        }
        //创建map集合，用于封装返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);
        //3.根据月份，从数据库过去12个月的会员数据
        List<Integer> memberCountByMonth = this.memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCountByMonth);
        //4.返回数据
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 套餐预约占比统计 ：
     * 1.需要查询出每个套餐的名称，<查询t_setmeal表>
     * 2.每个套餐的预约数量，<根据套餐名称进行分组查询，每个套餐的预约次数>
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {


        try {
            //1.调用service查询数据库中的数据
            List<Map<String, Object>> list = this.mealService.findSetmealCount();
            //2.创建map集合，用于保存查询的数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("setmealCount", list);//保存套餐预约数量
            ArrayList<String> setmealnames = new ArrayList<>();
            for (Map<String, Object> stringObjectHashMap : list) {
                String name = (String) stringObjectHashMap.get("name");
                setmealnames.add(name);//套餐名称
            }
            map.put("setmealNames", setmealnames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @Reference
    private ReportService reportService;

    /**
     * 运营数据统计
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusineseReportData() {
        try {
            Map<String, Object> reportData = this.reportService.getBusineseReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 导出运数据成excel表格
     *
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result reportBusineseReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.1查询运营数据
            Map<String, Object> result = this.reportService.getBusineseReportData();
            //1.2取出返回结果数据，将这些结果数据写入到excel表格中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //2.1动态获取模板文件在磁盘中的绝对路径，
            String template = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //2.2基于提供的excel模板文件在内存中创建一个excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(template)));

            //3.读取模板文件中的内容数据
            //3.1读取模板文件中的第一excel工作表
            XSSFSheet sheet = excel.getSheetAt(0);
            //获取对应需要填充数据的单元格对象，然后再设置对应的值
            XSSFRow row = sheet.getRow(2);//提高性能优化，只创建一个row对象
            row.getCell(5).setCellValue(reportDate);//日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数
            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数
            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //4.使用输出流进行表格下载，response返回输入流对象，基于浏览器为客户端下载
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表excel文件格式，ms-excel代表微软的excel文件
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//content-Disposition下载类型，attachment 附件类型下载，filename文件名
            excel.write(outputStream);
            outputStream.flush();
            outputStream.close();
            excel.close();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
}
