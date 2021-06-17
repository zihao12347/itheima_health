package com.itheima.test;


import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * Freemarker模板引擎测试：模板 + 数据  ----生成文本
 */
public class FreemarkerTest {
    public static void main(String[] args)throws Exception {
        //1.定义模板
        Configuration configuration = new Configuration(Configuration.getVersion());
        //1.1设置字符编码格式
        configuration.setDefaultEncoding("utf-8");
        //1.2设置模板文件的路径
        configuration.setDirectoryForTemplateLoading(new File("C:\\Users\\Y7000\\Desktop"));
        //1.3设置模板文件
        Template template = configuration.getTemplate("aa.ftl");
        //获取数据
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "黑马传智健康！");
        map.put("message", "zihao");
        //生成文本
        FileWriter fileWriter = new FileWriter("C:\\Users\\Y7000\\Desktop\\text.html");
        template.process(map, fileWriter);

    }
}
