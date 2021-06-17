package com.itheima.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 */
public interface UploadService {
    //上传二进制图片文件方法
    public String upload(byte[] source,String ext_name) throws Exception;
}
