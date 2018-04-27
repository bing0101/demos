package com.bing.retrofit.controller;

import com.bing.retrofit.dto.TestRequest;
import com.bing.retrofit.dto.TestResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bing on 2018/4/27.
 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public TestResponse test(@RequestBody TestRequest request) {

        TestResponse response = new TestResponse();
        response.setResponse("response:" + request.getRequest());
        return response;
    }
}
