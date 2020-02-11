package tt.okhttp.chain;

import java.io.IOException;
import tt.okhttp.bean.Response;

public interface Interceptor {

    Response intercept(InterceptorChain chain) throws IOException;
}
