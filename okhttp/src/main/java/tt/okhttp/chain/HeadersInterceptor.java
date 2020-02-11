package tt.okhttp.chain;

import java.io.IOException;
import java.util.Map;
import tt.okhttp.bean.Request;
import tt.okhttp.bean.Response;
import test.LogUtil;

public class HeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        LogUtil.i("Http头拦截器....");
        Request request = chain.call.request();
        Map<String, String> headers = request.headers();
        headers.put("Host", request.url().getHost());
        headers.put("Connection", "Keep-Alive");
        if (request.body() != null) {
            String contentType = request.body().contentType();
            if (contentType != null) {
                headers.put("Content-Type", contentType);
            }
            long contentLength = request.body().contentLength();
            if (contentLength != -1) {
                headers.put("Content-Length", Long.toString(contentLength));
            }
        }
        return chain.proceed();
    }

}
