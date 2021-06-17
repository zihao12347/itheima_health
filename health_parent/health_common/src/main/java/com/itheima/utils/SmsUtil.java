// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SmsUtil.java

package com.itheima.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import java.io.PrintStream;
import java.util.Map;

public class SmsUtil
{

    public SmsUtil()
    {
    }

    public static void sendCode(String phoneNum, String validCode)
    {
        try
        {
            Credential cred = new Credential("AKIDN4XllXwxcUWJrBM8l9ISZR5Tm9F5n6i2", "LQG7i3nJabL9vQfifhRxBKEtITP6XgBT");
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "", clientProfile);
            SendSmsRequest req = new SendSmsRequest();
            String appid = "1400489477";
            req.setSmsSdkAppid(appid);
            req.setSign(sign);
            String senderid = "";
            req.setSenderId(senderid);
            String session = "";
            req.setSessionContext(session);
            String extendcode = "";
            req.setExtendCode(extendcode);
            String templateID = "877721";
            req.setTemplateID(templateID);
            String phoneNumbers[] = {
                (new StringBuilder()).append("86").append(phoneNum).toString()
            };
            req.setPhoneNumberSet(phoneNumbers);
            String templateParams[] = {
                validCode
            };
            req.setTemplateParamSet(templateParams);
            SendSmsResponse res = client.SendSms(req);
            System.out.println(SendSmsResponse.toJsonString(res));
        }
        catch(TencentCloudSDKException e)
        {
            e.printStackTrace();
        }
    }

    public static void sendSuccessMessage(String phoneNum, Map map)
    {
        try
        {
            Credential cred = new Credential("AKIDN4XllXwxcUWJrBM8l9ISZR5Tm9F5n6i2", "LQG7i3nJabL9vQfifhRxBKEtITP6XgBT");
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "", clientProfile);
            SendSmsRequest req = new SendSmsRequest();
            String appid = "1400489477";
            req.setSmsSdkAppid(appid);
            req.setSign(sign);
            String senderid = "";
            req.setSenderId(senderid);
            String session = "";
            req.setSessionContext(session);
            String extendcode = "";
            req.setExtendCode(extendcode);
            String templateID = "906577";
            req.setTemplateID(templateID);
            String phoneNumbers[] = {
                (new StringBuilder()).append("86").append(phoneNum).toString()
            };
            req.setPhoneNumberSet(phoneNumbers);
            String templateParams[] = {
                map.get("name").toString(), map.get("orderDate").toString(), map.get("helpCode").toString(), map.get("orderDate").toString()
            };
            req.setTemplateParamSet(templateParams);
            SendSmsResponse res = client.SendSms(req);
            System.out.println(SendSmsResponse.toJsonString(res));
        }
        catch(TencentCloudSDKException e)
        {
            e.printStackTrace();
        }
    }

    public static final String secretId = "AKIDN4XllXwxcUWJrBM8l9ISZR5Tm9F5n6i2";
    public static final String secretKey = "LQG7i3nJabL9vQfifhRxBKEtITP6XgBT";
    public static final String sign = "传智珍爱健康";
}
