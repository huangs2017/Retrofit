package tt.okhttp;

import tt.okhttp.bean.Request;

public class OkHttpClient {

    private Dispatcher dispatcher;
    private ConnectionPool connectionPool;

    public OkHttpClient() {
        this(new Builder());
    }

    // 建造者模式
    public OkHttpClient(Builder builder) {
        dispatcher = builder.dispatcher;
        connectionPool = builder.connectionPool;
    }

    public Call newCall(Request request) {
        return new Call(this, request);
    }


    public static final class Builder {
        Dispatcher dispatcher = new Dispatcher();   // 队列 任务分发
        ConnectionPool connectionPool = new ConnectionPool();
    }


    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public ConnectionPool connectionPool() {
        return connectionPool;
    }


}
