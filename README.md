## Retrofit 动态代理 
1. 把我们所定义接口中的method转换成ServiceMethod；  
2. 通过serviceMethod，args获取到okHttpCall对象；  
3. 把okHttpCall进一步封装并返回Call对象。  

```java
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
```
