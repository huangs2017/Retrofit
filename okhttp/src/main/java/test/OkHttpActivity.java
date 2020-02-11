package test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import tt.okhttp.Call;
import tt.okhttp.Callback;
import tt.okhttp.OkHttpClient;
import tt.okhttp.R;
import tt.okhttp.bean.Request;
import tt.okhttp.bean.RequestBody;
import tt.okhttp.bean.Response;

public class OkHttpActivity extends AppCompatActivity {

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_activity);
        client = new OkHttpClient();
    }

    public void get(View view) {
        Request request = new Request.Builder()
                .url("http://www.kuaidi100.com/query?type=yuantong&postid=11111111111")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) {
                LogUtil.e("OkHttp get 请求结果--> "+ response.getBody());

            }
        });
    }

    public void post(View view) {
        RequestBody body = new RequestBody()
                .add("city", "长沙")
                .add("key", "13cb58f5884f9749287abbead9c658f2");
        Request request = new Request.Builder()
                .post(body)
                .url("http://restapi.amap.com/v3/weather/weatherInfo")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) {
                LogUtil.e("OkHttp post 请求结果--> "+ response.getBody());
            }
        });
    }

}
