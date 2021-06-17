package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

/**
 * 使用apache poi提供的API对microsoft office格式文档进行读和写的操作
 * 通常使用apache poi操做excel文档
 */
public class POITest {
    /**
     * 使用POI读取excel文档中的内容：通过遍历的方式读取
     */
    @Test
    public void testexcelread() throws IOException {
        //1.创建工作簿，加载指定文件
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\Y7000\\Desktop\\test.xlsx")));
        //2.获取工作簿中的工作表sheet,获取第一个sheet中的内容
        XSSFSheet sheet = excel.getSheetAt(0);
        //3.遍历工作表sheet的每一行对象
        for (Row row : sheet) {
            //4.遍历每行对象获取每个单元格对象
            for (Cell cell : row) {
                //输入获取每个单元格中的值
                cell.setCellType(Cell.CELL_TYPE_STRING);    //将每个单元格的数据格式都转化成string类型，防止数据格式报错
                System.out.println(cell.getStringCellValue());
            }
        }
        //5.关闭资源
        excel.close();
    }

    /**
     * 使用API读取excel文档中的内容2:
     * 通过工作表中最后一个行号和最后一个单元格索引进行遍历，提高读取效率
     *
     * @throws IOException
     */
   // @Test
    public void testexcelread2() throws IOException {
        //1.创建工作簿对象，加载excel文件
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\Y7000\\Desktop\\test.xlsx")));
        //2.获取工作簿中的工作表对象
        XSSFSheet sheet = excel.getSheetAt(0);
        //3.获取当前工作表中有数据的最后一行，从0开始获取
        int lastRowNum = sheet.getLastRowNum();
        //遍历每行对象，注意遍历行号的时候是<=
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            //4.获取当前行最后一个有值的单元格引索,从1开始
            short lastCellNum = row.getLastCellNum();
            //遍历单元格
            for (int j = 0; j < lastCellNum; j++) {
                //根据引索获取单元格对象
                XSSFCell cell = row.getCell(j);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                System.out.println(cell.getStringCellValue());

            }
        }
        //5.释放资源
        excel.close();
    }

    /**
     * 使用apache的POI向excel文件中写入数据：在内存中创建一个excel文件，并且通过输入流将内存中的excel写入到本地磁盘中
     */
    @Test
    public void testexcelwrite() throws IOException {
        //1.在内存中创建XSSFWorkbook工作簿对象
        XSSFWorkbook excel=new XSSFWorkbook();
        //2.创建XSSHSheet工作表对象
        XSSFSheet sheet=excel.createSheet("传智健康");
        //3.创建行对象
        XSSFRow row = sheet.createRow(0);
        //4.创建cell单元格对象
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("name");
        row.createCell(1).setCellValue("zihao");
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("age");
        row1.createCell(1).setCellValue("20");
        //创建一个输出流对象，将内存中的excel文件写入到磁盘中
        FileOutputStream outputStream=new FileOutputStream(new File("C:\\Users\\Y7000\\Desktop\\test2.xlsx"));
        //5.将内存中的excel写入到磁盘中
        excel.write(outputStream);
        outputStream.flush();
        //6.释放资源
        outputStream.close();
        excel.close();
    }
}
