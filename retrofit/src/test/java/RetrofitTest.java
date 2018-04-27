import com.alibaba.fastjson.JSON;
import com.bing.retrofit.api.TestService;
import com.bing.retrofit.dto.TestRequest;
import com.bing.retrofit.dto.TestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bing on 2018/4/27.
 */
public class RetrofitTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws InterruptedException {
        retrofitTest();
    }


    public static void retrofitTest() throws InterruptedException {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/").addConverterFactory(GsonConverterFactory.create(gson)).build();
        TestService testService = retrofit.create(TestService.class);

        TestRequest request = new TestRequest();
        request.setRequest("test");

        Call<TestResponse> call = testService.test(request);

        call.enqueue(new Callback<TestResponse>() {
            @Override
            public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {
                System.out.println(JSON.toJSONString(response.body()));
            }

            @Override
            public void onFailure(Call<TestResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
