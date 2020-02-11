package tt.okhttp.chain;

import java.io.IOException;
import java.util.List;
import tt.okhttp.Call;
import tt.okhttp.NetDataPipe;
import tt.okhttp.SocketConnect;
import tt.okhttp.bean.Response;

//拦截器链
public class InterceptorChain {

    final List<Interceptor> interceptors;
    final int index;
    final Call call;
    final SocketConnect socketConnect;
    final NetDataPipe netDataPipe = new NetDataPipe();

    public InterceptorChain(List<Interceptor> interceptors, int index, Call call, SocketConnect socketConnect) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.socketConnect = socketConnect;
    }

    public Response proceed() throws IOException {
        return proceed(socketConnect);
    }

    public Response proceed(SocketConnect socketConnect) throws IOException {
        Interceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors, index + 1, call, socketConnect);
        Response response = interceptor.intercept(next);
        return response;
    }

}
