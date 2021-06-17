package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传service
 */
@Service(interfaceClass = UploadService.class)  //发布service服务接口
public class UploadServiceImpl implements UploadService {

    private TrackerClient trackerClient;
    private TrackerServer trackerServer;
    private StorageServer storageServer;
    private StorageClient storageClient;

    /**
     * 上传二进制图片文件方法
     * @param source
     * @param ext_name
     * @return
     * @throws Exception
     */
    public String upload(byte[] source,String ext_name) throws Exception {
        //3.1初始化配置文件
        String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
        // 3.2、加载配置文件，配置文件中的内容就是 tracker 服务的地址。
        ClientGlobal.init(filePath);
        // 3.3、创建一个 TrackerClient 对象。直接 new 一个。
         trackerClient = new TrackerClient();
        // 3.4、使用 TrackerClient 对象创建连接，获得一个 TrackerServer 对象。
         trackerServer = trackerClient.getConnection();
        // 3.5、创建一个 StorageServer 的引用，值为 null
         storageServer = null;
        // 3.6、创建一个 StorageClient 对象，需要两个参数 TrackerServer 对象、StorageServer 的引用
         storageClient = new StorageClient(trackerServer, storageServer);
        // 3.7、使用 StorageClient 对象上传图片。
        String[] strings = storageClient.upload_file(source, ext_name, null);
        // 7、返回数组。包含组名和图片的路径。
        // 7、返回数组。包含组名和图片的路径。
        StringBuilder sb=new StringBuilder("http://198.23.153.251");
        for (String string : strings) {
            sb=sb.append("/"+string);
        }
        return sb.toString();
    }
}
