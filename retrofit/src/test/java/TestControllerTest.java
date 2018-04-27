import com.bing.retrofit.util.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bing on 2018/4/27.
 */
public class TestControllerTest {
    @Test
    public void test() {
        Map<String, String> param = new HashMap<>();
        param.put("request", "test");
        System.out.println(HttpUtil.post("http://localhost:8080/test", param));
    }
}
