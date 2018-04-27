package com.bing.retrofit.util;

import com.alibaba.fastjson.JSON;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by bing on 2017/12/7.
 */
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public static String post(String url, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();

        String result = null;
        long start = System.currentTimeMillis();
        try {
            start = System.currentTimeMillis();
            Response resp = okHttpClient.newCall(request).execute();
            if (resp.isSuccessful()) {//请求成功
                result = resp.body().string();
            } else {
                String msg = String.format("HTTP响应码：%s", resp.code());
                throw new RuntimeException(msg);
            }
        } catch (IOException e) {
            String msg = String.format("Http请求失败，url:%s, params:%s", url, JSON.toJSONString(params));
            logger.error(msg);
            throw new RuntimeException(msg, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logger.info("cost:{}", cost);
        }

        return result;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
