package tt.okhttp.chain;

import java.io.IOException;
import tt.okhttp.bean.Response;
import test.LogUtil;

public class RetryInterceptor implements Interceptor {

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        LogUtil.i("重试拦截器....");
        IOException exception = null;
        for (int i = 0; i < 3; i++) { // 默认重试3次
            try {
                Response response = chain.proceed();
                return response;
            } catch (IOException e) {
                exception = e;
            }
        }
        throw exception;
    }
}
