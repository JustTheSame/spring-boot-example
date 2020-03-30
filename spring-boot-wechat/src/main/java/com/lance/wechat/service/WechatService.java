package com.lance.wechat.service;


import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WechatService {

    public synchronized static String getTokenFromWx(String appId, String appSecret) {
        String accessToken = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);

        ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {
            @Override
            public JSONObject handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    if (null != entity) {
                        String result = EntityUtils.toString(entity);
                        JSONObject resultObj = JSONObject.fromObject(result);
                        return resultObj;
                    } else {
                        return null;
                    }
                } else {
                    throw new ClientProtocolException("Unexcepted response status: " + status);
                }

            }
        };
        try {
            JSONObject responseBody = httpClient.execute(httpGet, responseHandler);
            String token = (String) responseBody.get("access_token");
            Long accessTokenInvalidTime = Long.valueOf(responseBody.get("expires_in") + "");
            System.out.println("获得的accessToken为：" + token);
            System.out.println("accessToken有效期：" + accessTokenInvalidTime + "秒");
            accessToken = token;
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public static JSONObject sendHelloWorldMsg(String appId, String appSecret) {
        String jsonContext = "{"
                + "\"filter\": {"
                + "\"is_to_all\": true"
                + "},"
                + "\"text\":"
                + "{"
                + "\"content\":\"" +
                "微信公众号入门HelloWorld\r\n" +
                "========================" +
                "\r\n" +
                "(内容支持超链接,换行)\r\n" +
                "感谢您的支持！\r\n" +
                "作者：Tian。\r\n" +
                "博客：<a href='https://www.jianshu.com/p/01da0af773ed'>博客</a>\r\n" +
                "\""
                + "},"
                + "\"msgtype\": \"text\""
                + "}";
        String token = getTokenFromWx(appId, appSecret);
        System.out.println("token: " + token);
        boolean flag = false;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + token);
        StringEntity myEntity = new StringEntity(jsonContext, ContentType.create("text/plain", "UTF-8"));
        httpPost.setEntity(myEntity);
        ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {
            @Override
            public JSONObject handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    if (null != entity) {
                        String result = EntityUtils.toString(entity);
                        JSONObject resultObj = JSONObject.fromObject(result);
                        return resultObj;
                    } else {
                        return null;
                    }
                } else {
                    throw new ClientProtocolException("Unexcepted response status: " + status);
                }
            }
        };
        try {
            JSONObject responseBody = httpClient.execute(httpPost, responseHandler);
            System.out.println(responseBody);
            httpClient.close();
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        sendHelloWorldMsg("", "");
    }

}
