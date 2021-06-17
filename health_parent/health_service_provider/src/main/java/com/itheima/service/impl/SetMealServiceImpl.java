package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)   //发布服务
public class SetMealServiceImpl implements SetMealService {
    //注入dao
    @Autowired
    private SetMealDao setMealDao;

    //注入JedisPool连接池
    @Autowired
    private JedisPool jedisPool;

    //注入FreeMarker配置对象
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    //通过@Value("key")加载freemarker.properties属性配置文件中的value值
    @Value("${out_put_path}")
    private String outPutPath;      //静态页面生成的位置


    //设置套餐与检查组的关联关系
    public void setAssociation(Integer setmealid, Integer[] checkgroupids) {
        Map<String, Integer> map = null;
        if (checkgroupids != null && checkgroupids.length > 0) {
            for (Integer checkgroupid : checkgroupids) {
                map = new HashMap<>();
                map.put("setmealid", setmealid);
                map.put("checkgroupid", checkgroupid);
                setMealDao.setAssociation(map);
            }
        }
    }

    /**
     * 完善文件上传：
     * 2)将新增套餐上传的文件名称保存在redis数据库的SETMEAL_PIC_DB_RESOURCES集合中
     */
    public void savePic2Redis(String filename) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, filename);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //1.调用PageHelper插件提供的startePage(currentPage,pageSize)方法进行生成分页sql语句
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //2.调用dao的分页查询方法
        Page<Setmeal> setMealDaoPage = setMealDao.findPage(queryPageBean.getQueryString());
        //获取每页展示的数据集合数据
        List<Setmeal> setmealList = setMealDaoPage.getResult();
        //获取总记录数
        long total = setMealDaoPage.getTotal();
        return new PageResult(total, setMealDaoPage);
    }

    /**
     * 移动端获取套餐数据列表
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> setmealList = this.setMealDao.findAll();
        return setmealList;
    }

    /**
     * 根据套餐id获取套餐详情数据
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findMealDeatailById(Integer id) {
        Setmeal setmeal = this.setMealDao.findMealDeatailById(id);
        return setmeal;
    }

    public Setmeal findById(Integer id) {
        Setmeal setmeal = this.setMealDao.findById(id);
        return setmeal;
    }

    /**
     * 查询预约套餐占比数量
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> setmealCount = this.setMealDao.findSetmealCount();
        return setmealCount;
    }

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] ids) throws IOException, TemplateException {
        setMealDao.add(setmeal);
        setAssociation(setmeal.getId(), ids);
        //调用savePic2Redis方法，将新增套餐上传的图片名称保存在redis的SETMEAL_PIC_DB_RESOURCES集合中
        savePic2Redis(setmeal.getImg());
        //当后台系统修改了套餐数据时（如，套餐新增，套餐修改，套餐删除；当前只有套餐新增操作），需要重新生成静态页面
        generateMoblieStaticHtml();
    }

    //生成哪些的静态页面
    public void generateMoblieStaticHtml() throws IOException, TemplateException {
        //在生成静态页面之前生成查询数据
        List<Setmeal> list = this.setMealDao.findAll();//查询套餐列表数据

        //1.生成套餐列表页面
        generateMobileSetMealListHtml(list);
        //2.生成套餐详情页面
        generateMobileSetMealDetailHtml(list);
    }

    //生成套餐列表静态页面
    public void generateMobileSetMealListHtml(List<Setmeal> list) throws IOException, TemplateException {
        //1.调用静态页面方法的通用模板生成html静态文件
        Map map = new HashMap<>();
        map.put("setmealList",list);
        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    //生成套餐详情静态页面(可能又多个)
    public void generateMobileSetMealDetailHtml(List<Setmeal> list) throws IOException, TemplateException {
        for (Setmeal setmeal : list) {
            Map map=new HashMap();
            map.put("setmeal", findMealDeatailById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_"+setmeal.getId()+".html", map);
        }

    }


    /**
     * 生成静态页面方法的通用模板
     *
     * @param templateName //模板名称
     * @param htmlPageName //生成的静态页面名称
     * @param map          //模型数据：页面静态化技术的模型数据默认为Map类型
     */
    public void generateHtml(String templateName, String htmlPageName, Map map) throws IOException, TemplateException {
        //1.创建configuration配置对象,当前是通过freeMarkConfigurer对象的getConfiguration()方法获取配置对象，不是传统的new出的对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //2.所在ftl模板所在的路径（已经在spring-service配置文件中，spring与freemark整合中配置完成）
        //3.设置字符集（  已经在spring-service配置文件中，spring与freemark整合中配置完成）
        //4加载模板文件
        Template template = configuration.getTemplate(templateName);
        //5.创建数据模型map
        //6.创建writer输出流对象

        FileWriter writer = new FileWriter(new File(outPutPath+"/"+htmlPageName));
        System.out.println(outPutPath+"/"+htmlPageName );
        //7.通过process()方法生成，
        template.process(map, writer);
        writer.close();//关闭输出流
    }
}
