package com.itheima.reggie.utils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import java.util.*;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import org.springframework.stereotype.Component;
public class SmsUtil {
    public static void sendSms(String phone,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                "LTAI5tNdBALSJfy7bLi8r46g",
                "8HwJR5NqhGCYloCWogXaXwXc3sdS75");
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"),       // The AccessKey ID of the RAM account
         System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"),   // The AccessKey Secret of the RAM account
         System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN"));     // STS Token
         **/

        IAcsClient client = new DefaultAcsClient(profile);


        SendSmsRequest request = new SendSmsRequest();
        request.setSignName("阿里云短信测试");
        request.setTemplateCode("SMS_154950909");
        request.setPhoneNumbers(phone);
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}

