package tt.okhttp;

import java.io.IOException;
import java.util.ArrayList;

import tt.okhttp.bean.Request;
import tt.okhttp.bean.Response;
import tt.okhttp.chain.CallServiceInterceptor;
import tt.okhttp.chain.ConnectionInterceptor;
import tt.okhttp.chain.HeadersInterceptor;
import tt.okhttp.chain.Interceptor;
import tt.okhttp.chain.InterceptorChain;
import tt.okhttp.chain.RetryInterceptor;

public class Call {

    OkHttpClient client;
    Request request;

    boolean executed; // 是否执行过

    public Call(OkHttpClient client, Request request) {
        this.client = client;
        this.request = request;
    }


    public Call enqueue(Callback callback) {
        executed = true;
        client.dispatcher().enqueue(new Task(callback));
        return this;
    }


    final class Task implements Runnable {
        Callback callback;
        public Task(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                Response response = getResponse();
                callback.onResponse(response);
            } catch (IOException e) {
                callback.onFailure(e);
            } finally {
                client.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.url().host;
        }
    }

    Response getResponse() throws IOException {
        ArrayList<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new RetryInterceptor());
        interceptors.add(new HeadersInterceptor());
        interceptors.add(new ConnectionInterceptor());
        interceptors.add(new CallServiceInterceptor());
        InterceptorChain interceptorChain = new InterceptorChain(interceptors, 0, this, null);
        return interceptorChain.proceed();
    }


    public Request request() {
        return request;
    }

    public OkHttpClient client() {
        return client;
    }


}
