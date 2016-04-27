package it.jdark.android.retrofit;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import it.jdark.android.retrofit.Utils.HostSelectionInterceptor;
import it.jdark.android.retrofit.databinding.MainActivityBinding;
import it.jdark.android.retrofit.modelView.ModelView;
import it.jdark.android.retrofit.pojo.Model;
import it.jdark.android.retrofit.rest.RetrofitWeather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private final String LOG = getClass().getSimpleName();

    private final String API_KEY = "931d25ea8d06c2a5cc3bed22c2a0cdac";
    private final String TARGET_REQUEST = "Rimini,it";


    @Inject @Named("Static") Retrofit retrofit;
    @Inject HostSelectionInterceptor dynamicClient;
    MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((MyApplication) getApplication()).getComponent().inject(this);

        Callback<Model> callback = new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                int resCode= response.code();
                Log.i(LOG, "Response code: " + resCode);
                Model data = response.body();

                ModelView modelView = new ModelView(data);  // Init ModelView
                binding.setModelView(modelView);            // Binding ModelView to the Binding
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.w(LOG, "-- Request: onFailure");
                Log.w(LOG, "-- " + t.getMessage());

            }
        };

        // Equivalent
        retrofit.create(RetrofitWeather.class).getWeatherReport(TARGET_REQUEST, API_KEY).enqueue(callback);
//        retrofit.create(RetrofitWeather.class).getWeatherReportFromDynamicUrl("weather?q="+TARGET_REQUEST+"&appid="+API_KEY).enqueue(callback);

        // Dynamic url example
//        retrofit.create(RetrofitWeather.class).getWeatherReportFromDynamicUrl("http://www.faerdf.com/weather?q="+TARGET_REQUEST+"&appid="+API_KEY).enqueue(callback);
//        retrofit.create(RetrofitWeather.class).getWeatherReportFromDynamicUrl("subdomain/weather?q="+TARGET_REQUEST+"&appid="+API_KEY).enqueue(callback);
    }
}
