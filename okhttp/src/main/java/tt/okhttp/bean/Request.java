package tt.okhttp.bean;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String method;                  // 请求方式 get/post
    private HttpUrl url;                    // 解析url 成HttpUrl 对象
    private Map<String, String> headers;    // 请求头
    private RequestBody body;               // 请求体

    public Request(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.headers = builder.headers;
        this.body = builder.body;
    }


    //---------------------------------------------------------------
    // 建造者模式
    public final static class Builder {

        String method;
        HttpUrl url;
        Map<String, String> headers = new HashMap<>();
        RequestBody body;

        public Builder get() {
            method = "GET";
            return this;
        }

        public Builder post(RequestBody body) {
            this.body = body;
            method = "POST";
            return this;
        }

        public Builder url(String url) {
            this.url = new HttpUrl(url);
            return this;
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder removeHeader(String name) {
            headers.remove(name);
            return this;
        }

        public Request build() {
            if (TextUtils.isEmpty(method)) {
                method = "GET";
            }
            return new Request(this);
        }

    }
    //---------------------------------------------------------------



    public String method() {
        return method;
    }

    public HttpUrl url() {
        return url;
    }

    public RequestBody body() {
        return body;
    }

    public Map<String, String> headers() {
        return headers;
    }


}
