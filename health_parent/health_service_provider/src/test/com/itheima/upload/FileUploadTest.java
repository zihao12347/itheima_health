package com.itheima.upload;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class FileUploadTest {
    @Test
    public void testupload() throws IOException, MyException {
        String uploadFilePath="C:\\Users\\Y7000\\Desktop\\ccdf4bcffc790aaeccf3aced033c40eb.mp4";
        String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
        // 1、加载配置文件，配置文件中的内容就是 tracker 服务的地址。
        ClientGlobal.init(filePath);
        // 2、创建一个 TrackerClient 对象。直接 new 一个。
        TrackerClient trackerClient = new TrackerClient();
        // 3、使用 TrackerClient 对象创建连接，获得一个 TrackerServer 对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建一个 StorageServer 的引用，值为 null
        StorageServer storageServer = null;
        // 5、创建一个 StorageClient 对象，需要两个参数 TrackerServer 对象、StorageServer 的引用
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 6、使用 StorageClient 对象上传图片。
        //扩展名不带“.”
        String[] strings = storageClient.upload_file(uploadFilePath, "mp4",
                null);
        // 7、返回数组。包含组名和图片的路径。
        StringBuilder sb=new StringBuilder("http://198.23.153.251");
        for (String string : strings) {
            sb=sb.append("/"+string);
        }
        System.out.println(sb);
    }
}
