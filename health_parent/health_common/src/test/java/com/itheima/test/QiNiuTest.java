package com.itheima.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * 使用七牛云来完成文件上传的测试功能
 */
public class QiNiuTest {
    /**
     * 使用七牛云提供的SDK完成将本地图片上传到七牛云服务器
     */
   // @Test
    public void uploadtest() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //密钥
        String accessKey = "DwWFQWC-PlGlpvpO4GyYa5wpO4dOxyuGIjhl6B8o";
        String secretKey = "qrDwRYRU0vW40VyuNrIT9OCBGbCmUoab_2UVM7GH";
        //七牛云的对象存储空间的实例名称
        String bucket = "zihaospace1";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        /**
         * 注意：每次上传图片的时候只能上传一张图片问，不能上传文件夹，需要上传文件夹的内容时，需要将文件夹内容依次递归然后上传
         */
        String localFilePath = "D:\\QiniuTest\\ed8fda29e417ed8ae9ba1310ed30314d.png";   //本地图片地址
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        /**
         * 通常在文件上传内容很多的时候，为确保每个图片文件的唯一性，不指定名称，使用hash值为文件名称
         */
        String key = "heimalearningway.png";//文件上传后的名称，格式

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

    /**
     * 使用七牛云提供的SDK完成删除七牛云服务器空间中的文件
     */
    @Test
    public void deleteTest(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释

        String accessKey = "DwWFQWC-PlGlpvpO4GyYa5wpO4dOxyuGIjhl6B8o";
        String secretKey = "qrDwRYRU0vW40VyuNrIT9OCBGbCmUoab_2UVM7GH";

        String bucket = "zihaospace1";
        String key = "heimalearningway.png";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }

    }
}
