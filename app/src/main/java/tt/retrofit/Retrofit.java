package tt.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import tt.okhttp.OkHttpClient;

public class Retrofit {

    Map<Method, ServiceMethod> serviceMethodCache = new HashMap<>();
    String baseUrl;
    OkHttpClient okHttpClient;


    Retrofit(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.okHttpClient = builder.okHttpClient;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public <T> T create(final Class<T> aClass) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                ServiceMethod serviceMethod = loadServiceMethod(method);
                return serviceMethod.toCall(args);
            }
        };
        return (T) Proxy.newProxyInstance(aClass.getClassLoader(), new Class<?>[]{aClass}, handler);
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodCache.get(method);
        if (serviceMethod == null) {
            serviceMethod = new ServiceMethod.Builder(this, method).build();
            serviceMethodCache.put(method, serviceMethod);
        }
        return serviceMethod;
    }


    public static class Builder {
        String baseUrl;
        OkHttpClient okHttpClient;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Retrofit build() {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
            }
            return new Retrofit(this);
        }

    }


}
