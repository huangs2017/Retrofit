package tt.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import tt.okhttp.Call;
import tt.okhttp.OkHttpClient;
import tt.okhttp.bean.Request;
import tt.okhttp.bean.RequestBody;
import tt.retrofit.annotation.Field;
import tt.retrofit.annotation.GET;
import tt.retrofit.annotation.POST;
import tt.retrofit.annotation.Query;

public class ServiceMethod {

    String baseUrl;
    OkHttpClient okHttpClient;
    String httpMethod;
    String relativeUrl;
    String[] parameters;

    String httpUrl;

    public ServiceMethod(Builder builder) {
        baseUrl = builder.retrofit.getBaseUrl();
        okHttpClient = builder.retrofit.getOkHttpClient();

        httpMethod = builder.httpMethod;
        relativeUrl = builder.relativeUrl;
        parameters = builder.parameters;
    }

    public Call toCall(Object[] args) {
        RequestBody body = new RequestBody();
        httpUrl = baseUrl + relativeUrl;

        for (int i = 0; i < parameters.length; i++) {
            if (httpMethod.equals("GET")) {
                if (i == 0) {
                    httpUrl = httpUrl + "?";
                } else {
                    httpUrl = httpUrl + "&";
                }
                httpUrl = httpUrl + parameters[i] + "=" + args[i];
            } else if (httpMethod.equals("POST")) {
                body.add(parameters[i], (String) args[i]); // 添加post请求参数
            }
        }

        Request request = null;
        if (httpMethod.equals("GET")) {
            request = new Request.Builder()
                    .url(httpUrl)
                    .build();
        } else if (httpMethod.equals("POST")) {
            request = new Request.Builder()
                    .post(body)
                    .url(httpUrl)
                    .build();
        }
        // 请求
        return okHttpClient.newCall(request);
    }

    //-----------------------------------------------------------------------
    public static class Builder {
        final Retrofit retrofit;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotations; // 方法参数的注解

        String httpMethod;
        String relativeUrl;
        String[] parameters;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.methodAnnotations = method.getAnnotations();
            this.parameterAnnotations = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            for (Annotation methodAnnotation : methodAnnotations) {
                parseMethodAnnotation(methodAnnotation);
            }
            parameters = new String[parameterAnnotations.length];
            for (int i = 0; i < parameterAnnotations.length; i++) {
                // 获得参数上注解的值 Query("city")
                Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    String name = null;
                    if (annotation instanceof Query) {
                        name = ((Query) annotation).value();
                    } else if (annotation instanceof Field) {
                        name = ((Field) annotation).value();
                    }
                    parameters[i] = name;
                }
            }
            return new ServiceMethod(this);
        }


        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                this.httpMethod = "GET";
                this.relativeUrl = ((GET) annotation).value();
            } else if (annotation instanceof POST) {
                this.httpMethod = "POST";
                this.relativeUrl = ((POST) annotation).value();
            }
        }

    }
    //-----------------------------------------------------------------------

}
