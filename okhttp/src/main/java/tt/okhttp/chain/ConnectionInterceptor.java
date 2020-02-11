package tt.okhttp.chain;

import java.io.IOException;
import tt.okhttp.OkHttpClient;
import tt.okhttp.SocketConnect;
import tt.okhttp.bean.HttpUrl;
import tt.okhttp.bean.Request;
import tt.okhttp.bean.Response;
import test.LogUtil;

public class ConnectionInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        LogUtil.i("连接拦截器....");
        Request request = chain.call.request();
        HttpUrl url = request.url();
        String host = url.getHost();
        int port = url.getPort();

        OkHttpClient client = chain.call.client();
        SocketConnect socketConnect = client.connectionPool().get(host, port);  // 重点====================
        if (socketConnect == null) {
            socketConnect = new SocketConnect();
        }

        try {
            Response response = chain.proceed(socketConnect);  // 下一个拦截器
            if (response.isKeepAlive()) { // 保持连接
                client.connectionPool().put(socketConnect);                     // 重点====================
            }
            return response;
        } catch (IOException e) {
            throw e;
        }
    }
}
