package test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import tt.okhttp.Call;
import tt.okhttp.Callback;
import tt.okhttp.bean.Response;
import tt.retrofit.R;
import tt.retrofit.Retrofit;
import tt.retrofit.annotation.Field;
import tt.retrofit.annotation.GET;
import tt.retrofit.annotation.POST;
import tt.retrofit.annotation.Query;

public class RetrofitActivity extends AppCompatActivity {

    Retrofit retrofit;
    Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_activity);
        retrofit = new Retrofit.Builder().baseUrl("http://restapi.amap.com/").build();
        weather = retrofit.create(Weather.class);
    }

    interface Weather {
        @GET("v3/weather/weatherInfo")
        Call get(@Query("city") String city, @Query("key") String key);

        @POST("v3/weather/weatherInfo")
        Call post(@Field("city") String city, @Field("key") String key);
    }

    public void get(View view) {
        Call call1 = weather.get("长沙", "13cb58f5884f9749287abbead9c658f2");
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) {
                LogUtil.e("Retrofit get 请求结果--> "+ response.getBody());
            }
        });
    }

    public void post(View view) {
        Call call2 = weather.post("长沙", "13cb58f5884f9749287abbead9c658f2");
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) {
                LogUtil.e("Retrofit post 请求结果--> "+ response.getBody());
            }
        });
    }

}
