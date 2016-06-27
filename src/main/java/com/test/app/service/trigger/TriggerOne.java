package com.test.app.service.trigger;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.test.app.service.filter.AffixFilterOne;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.*;

/**
 * 触发 上传七牛图片所占比例 监控邮件
 */
@Component
public class TriggerOne {
    private static final Log LOGGER = LogFactory.getLog(TriggerOne.class);

    @Autowired
    private AffixFilterOne affixFilterOne;

    @Value("${action.sendemail.pubUrl}")
    private String pubUrl;
    @Value("${action.sendemail.prodUrl}")
    private String prodUrl;
    @Value("${action.messages.address}")
    private String address;
    @Value("${action.messages.subject}")
    private String subject;


    public void execute() throws Exception {
        LOGGER.info("查找出所有1天前用户上传图片的记录");
        List<String> urlList = new ArrayList<String>();
        urlList = affixFilterOne.execute();
        if (urlList.size() == 0) {
            LOGGER.info("昨日用户上传图片数为0");
        } else {
            LOGGER.info("查询执行完毕");
        }

        //发送http请求验证每个图片是否成功上传至七牛服务器,成功加1
        if (urlList != null && urlList.size() > 0) {
            int num = 0;
            int totalNum = urlList.size();
            List<String> failedList = new ArrayList<>();
            for (String url : urlList) {
                HttpPost httppost = new HttpPost(url);
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpResponse response = httpclient.execute(httppost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    num++;
                } else {
                    failedList.add(url);
                }
            }
            double value = num / totalNum;
            String percentValue = NumberFormat.getPercentInstance().format(value);
            sendEmail(num, totalNum, percentValue, failedList);
        }
    }

    //发邮件把占用比例发出来
    public void sendEmail(Integer num, Integer totalNum, String percentValue, List<String> failedList) throws Exception {

        //监控日期
        DateTime today = new DateTime(new Date());
        DateTime yesterday = today.minusDays(1);
        String time = yesterday.toString("yyyy-MM-dd");

        //拼接每条上传失败的url
        StringBuffer sb = new StringBuffer();
        for (String failedUrl : failedList) {
            sb.append(failedUrl).append("<br />");
        }
        //邮件内容
        String s = time + "日监控信息:<br />" +
                "用户当日上传所有图片的记录为: " + totalNum + "<br />" +
                "成功上传至七牛云存储上的图片记录为: " + num + "<br />" +
                "成功上传至七牛云存储上的图片占用户当日上传所有图片的比例为: " + percentValue + "<br />" +
                "未成功上传至七牛云存储上图片的URL记录有:<br />" + sb.toString();

        String s1 = HtmlUtils.htmlEscape(s);
        LOGGER.info("邮件内容拼接成功");
        String content = HtmlUtils.htmlUnescape(s1);
        LOGGER.info("邮件内容HTML格式转换成功");

        //调用发送邮件接口
        CloseableHttpClient httpclient1 = HttpClients.createDefault();
        //上线后换成线上域名prodUrl
        HttpPost httpPost = new HttpPost(prodUrl);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("to", address));
        nvps.add(new BasicNameValuePair("subject", subject));
        nvps.add(new BasicNameValuePair("content", content));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  //设置参数
            CloseableHttpResponse response1 = httpclient1.execute(httpPost); //执行

            try {
                HttpEntity entity1 = response1.getEntity();
                String result = EntityUtils.toString(entity1);
                LOGGER.info("邮件发送成功,返回结果为: " + result);
            } finally {
                response1.close();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


}

