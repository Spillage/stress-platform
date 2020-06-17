package com.xielu.arch.platform.server.util;

import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class.getName());

    public static final OkHttpClient client = new OkHttpClient();
    public static final okhttp3.MediaType MEDIA_TYPE_MARKDOWN = okhttp3.MediaType.parse("application/json; charset=utf-8");

    /**
     * 不包含特殊header的get请求
     *
     * @return
     */
    @SneakyThrows
    public String doGet(String url) {
        String result = "";
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                LOGGER.error("GET without Header Request Fail: " + response);
            }
            result = response.body().string();
            LOGGER.debug(result);

        }catch (Exception e){
            LOGGER.error("GET without Header Request Fail: ",e);
        }finally {
            return StringUtils.isEmpty(result)?"":result;
        }
    }

    /**
     * 包含特殊header的get请求
     *
     * @return
     */
    public String doGetWithHeader(String url, Map<String, String> headers) {
        String result = "";
        try {
            Headers headerbuild = Headers.of(headers);
            Request request = new Request.Builder().url(url).headers(headerbuild).
                    build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
            LOGGER.debug(result);
        } catch (Exception e) {
            LOGGER.error("GET with Header Request Fail: " + e);
        } finally {
            return result;
        }
    }

    /**
     * 不包含header的post请求
     *
     * @return
     */
    public String doPost(String url, String jsonStr) {
        String result = "";
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonStr))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                LOGGER.error("POST without Header Request Fail: " + response);
            }
            result = response.body().string();
            LOGGER.debug(result);
        } catch (Exception e) {
            LOGGER.error("POST without Header Request Fail: " + e);
        } finally {
            return result;
        }
    }

    /**
     * 包含header的post请求
     *
     * @return
     */
    public String doPostWithHeader(String url, Map<String, String> headers, String jsonStr) {
        String result = "";
        try {
            Headers headerbuild = Headers.of(headers);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headerbuild)
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonStr))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                LOGGER.error("POST with Header Request Fail: " + response);
            }
            result = response.body().string();
            LOGGER.debug(result);
        }catch (Exception e){
            LOGGER.error("POST with Header Request Fail: " + e);
        }finally {
            return result;
        }
    }

    /**
     * 获取工单会话信息
     * post 但没有body
     * @param url
     * @param headers
     * @return
     */
    @SneakyThrows
    public String doPostWithHeaderAndNoBody(String url,Map<String,String> headers){
        String result = "";
        try{
            Headers headerbuild = Headers.of(headers);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headerbuild)
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, ""))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                LOGGER.error("POST with Header Request Fail: " + response);
            }
            result = response.body().string();
            LOGGER.debug(result);
        }catch (Exception e){
            LOGGER.error("doPostWithHeaderAndNoBody error: ",e);
        }finally {
            return result;
        }
    }
}
