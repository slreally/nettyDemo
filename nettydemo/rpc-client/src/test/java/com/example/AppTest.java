package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cotroller.TestService;
import com.example.pojo.User;
import com.sun.javafx.fxml.builder.URLBuilder;
import junit.framework.TestCase;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.L;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    @Autowired
    private TestService testService;

    /**
     * Get 有参1
     * @throws URISyntaxException
     */
    @org.junit.jupiter.api.Test
    public void test1() throws URISyntaxException {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
//        String url = "http://127.0.0.1:8196/workbench/count";
        String url = "http://192.168.0.145:8085/workbench/count";
        //塞入form参数
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter("satId", "10");
        //创建httpGet远程连接实例,这里传入目标的网络地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        //设置请求头信息，鉴权(没有可忽略)
        httpGet.setHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLns7vnu5_nrqHnkIblkZgiLCJ1c2VyUmVxdWVzdElwIjoiMTkyLjE2OC4wLjg0IiwiYWNjb3VudE5vIjoiYWRtaW4iLCJ1c2VyTmFtZSI6Iuezu-e7n-euoeeQhuWRmCIsImV4cCI6MTY3NTc4NTYwMCwidXNlcklkIjoiMmM5MDgxNWM3YWE5ZjU1NTAxN2FhOWZlZDE4NjAwMDEiLCJpYXQiOjE2NzU3MzE3NjgsImp0aSI6IjJjOTA4MTVjN2FhOWY1NTUwMTdhYTlmZWQxODYwMDAxIn0.rtlOiliHjRWEojcnhCSjlnn3RKZwWATgKKHFaEbyTRQ");
        // 设置配置请求参数(没有可忽略)
        RequestConfig requestConfig = RequestConfig.custom()
                // 连接超时时间
                .setConnectTimeout(35000)
                // 请求超时时间
                .setConnectionRequestTimeout(35000)
                // Socket读写超时时间
                .setSocketTimeout(60000)
                .build();
        // 为httpGet实例设置配置
        httpGet.setConfig(requestConfig);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            //获取Response状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("状态码 is " + statusCode);
            //获取响应实体
            HttpEntity entity = response.getEntity();
            System.out.println("响应内容长度为 + " + entity.getContentLength());
            //将响应实体转为字符串
            String msg = EntityUtils.toString(entity);
            System.out.println("响应的字符串json is " + msg);
            JSONObject jsonObject = JSON.parseObject(msg);
        } catch (IOException e) {
            System.out.println("连接失败"+e.getMessage());
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get 有参2
     * @throws URISyntaxException
     */
    @org.junit.jupiter.api.Test
    public void test2() throws URISyntaxException {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("satId", "10"));
        URI uri = new URIBuilder().setScheme("http")
                .setHost("192.168.0.145")
                .setPort(8085)
                .setPath("/workbench/count")
                .setParameters(pairs)
                .build();
        //创建httpGet远程连接实例,这里传入目标的网络地址
        HttpGet httpGet = new HttpGet(uri);
        //设置请求头信息，鉴权(没有可忽略)
        httpGet.setHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLns7vnu5_nrqHnkIblkZgiLCJ1c2VyUmVxdWVzdElwIjoiMTkyLjE2OC4wLjg0IiwiYWNjb3VudE5vIjoiYWRtaW4iLCJ1c2VyTmFtZSI6Iuezu-e7n-euoeeQhuWRmCIsImV4cCI6MTY3NTc4NTYwMCwidXNlcklkIjoiMmM5MDgxNWM3YWE5ZjU1NTAxN2FhOWZlZDE4NjAwMDEiLCJpYXQiOjE2NzU3MzE3NjgsImp0aSI6IjJjOTA4MTVjN2FhOWY1NTUwMTdhYTlmZWQxODYwMDAxIn0.rtlOiliHjRWEojcnhCSjlnn3RKZwWATgKKHFaEbyTRQ");
        // 设置配置请求参数(没有可忽略)
        RequestConfig requestConfig = RequestConfig.custom()
                // 连接超时时间
                .setConnectTimeout(35000)
                // 请求超时时间
                .setConnectionRequestTimeout(35000)
                // Socket读写超时时间
                .setSocketTimeout(60000)
                .build();
        // 为httpGet实例设置配置
        httpGet.setConfig(requestConfig);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            //获取Response状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("状态码 is " + statusCode);
            //获取响应实体
            HttpEntity entity = response.getEntity();
            System.out.println("响应内容长度为 + " + entity.getContentLength());
            //将响应实体转为字符串
            String msg = EntityUtils.toString(entity);
            System.out.println("响应的字符串json is " + msg);
            JSONObject jsonObject = JSON.parseObject(msg);
        } catch (IOException e) {
            System.out.println("连接失败"+e.getMessage());
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Post 有参
     * @throws URISyntaxException
     */
    @org.junit.jupiter.api.Test
    public void test3() throws URISyntaxException, UnsupportedEncodingException {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:8196/check/algs");
        JSONObject param = new JSONObject();
        param.put("checkId", "a");
        param.put("checkName", "split");
        StringEntity stringEntity = new StringEntity(param.toJSONString(), "UTF-8");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-type", "application/json");
        //设置请求头信息，鉴权(没有可忽略)
        httpPost.setHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLns7vnu5_nrqHnkIblkZgiLCJ1c2VyUmVxdWVzdElwIjoiMTkyLjE2OC4wLjg0IiwiYWNjb3VudE5vIjoiYWRtaW4iLCJ1c2VyTmFtZSI6Iuezu-e7n-euoeeQhuWRmCIsImV4cCI6MTY3NTc4NTYwMCwidXNlcklkIjoiMmM5MDgxNWM3YWE5ZjU1NTAxN2FhOWZlZDE4NjAwMDEiLCJpYXQiOjE2NzU3MzE3NjgsImp0aSI6IjJjOTA4MTVjN2FhOWY1NTUwMTdhYTlmZWQxODYwMDAxIn0.rtlOiliHjRWEojcnhCSjlnn3RKZwWATgKKHFaEbyTRQ");
        // 设置配置请求参数(没有可忽略)
        RequestConfig requestConfig = RequestConfig.custom()
                // 连接超时时间
                .setConnectTimeout(35000)
                // 请求超时时间
                .setConnectionRequestTimeout(35000)
                // Socket读写超时时间
                .setSocketTimeout(60000)
                .build();
        // 为httpGet实例设置配置
        httpPost.setConfig(requestConfig);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpPost);
            //获取Response状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("状态码 is " + statusCode);
            //获取响应实体
            HttpEntity entity = response.getEntity();
            System.out.println("响应内容长度为 + " + entity.getContentLength());
            //将响应实体转为字符串
            String msg = EntityUtils.toString(entity);
            System.out.println("响应的字符串json is " + msg);
            JSONObject jsonObject = JSON.parseObject(msg);
        } catch (IOException e) {
            System.out.println("连接失败"+e.getMessage());
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        float a = (float)1.2;
        System.out.println(a);
        double b = a;
        System.out.println(b);

    }
}
