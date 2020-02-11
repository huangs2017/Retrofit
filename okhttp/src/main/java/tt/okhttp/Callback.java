package tt.okhttp;

import tt.okhttp.bean.Response;

public interface Callback {
    void onFailure(Throwable throwable);

    void onResponse(Response response);
}
