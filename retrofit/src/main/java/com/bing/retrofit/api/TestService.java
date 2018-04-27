package com.bing.retrofit.api;

import com.bing.retrofit.dto.TestRequest;
import com.bing.retrofit.dto.TestResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by bing on 2018/4/27.
 */
public interface TestService {
    @POST("test")
    Call<TestResponse> test(@Body TestRequest request);
}
