package it.jdark.android.retrofit;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import it.jdark.android.retrofit.databinding.MainActivityBinding;
import it.jdark.android.retrofit.modelView.ModelView;
import it.jdark.android.retrofit.pojo.Model;
import it.jdark.android.retrofit.rest.RetrofitWeather;
import it.jdark.android.retrofit.utils.HostSelectionInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private final String LOG = getClass().getSimpleName();

    private final String API_KEY = "931d25ea8d06c2a5cc3bed22c2a0cdac";
    private final String TARGET_REQUEST = "Rimini,it";


    @Inject @Named("Dynamic") Retrofit retrofitDynamic;
    @Inject @Named("Static") Retrofit retrofitStatic;

    @Inject HostSelectionInterceptor dynamicClient;
    MainActivityBinding binding;

    Callback<Model> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((MyApplication) getApplication()).getComponent().inject(this);

        // Lambda Expression
//        binding.fabDynamic.setOnClickListener(view -> Snackbar.make(view, "Dynamic Url Request!", Snackbar.LENGTH_LONG).setAction("Dynamic", DynamicRequest()).show());
//        binding.fabStatic.setOnClickListener(view -> Snackbar.make(view, "Static Url Request!", Snackbar.LENGTH_LONG).setAction("Static", StaticRequest()).show())
        binding.fabDynamic.setOnClickListener(v -> {Snackbar.make(v, "Dynamic Url Request", Snackbar.LENGTH_LONG).show(); DynamicRequest(); });
        binding.fabStatic.setOnClickListener(v -> {Snackbar.make(v, "Static Url Request", Snackbar.LENGTH_LONG).show(); StaticRequest(); });

        // Old Style expression
//        binding.fab.setOnClickListener(new android.view.View.OnClickListener() {
//            @Override
//            public void onClick(android.view.View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        callback = new Callback<Model>() {

            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                int resCode= response.code();
                Log.i(LOG, "Response code: " + resCode);
                Log.d(LOG, "Call -> " + call.request());

                // Handle error code from "openwethermap"
                // Responce is not a valid json for parsing!
                if (response.code() != 200) {
                    binding.setModelView(null);
                } else {
                    Model data = response.body();

                    ModelView modelView = new ModelView(data);  // Init ModelView
                    binding.setModelView(modelView);            // Binding ModelView to the Binding
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.w(LOG, "-- Request: onFailure");
                Log.d(LOG, "Call -> " + call.request());
                Log.w(LOG, "-- " + t.getMessage());

                // Update View value to null
                binding.setModelView(null);
            }
        };
    }

    private void StaticRequest() {
        if (binding.checkBox.isChecked()) {
            retrofitStatic.create(RetrofitWeather.class).getWeatherReport(TARGET_REQUEST, "None").enqueue(callback);
        } else {
            retrofitStatic.create(RetrofitWeather.class).getWeatherReport(TARGET_REQUEST, API_KEY).enqueue(callback);
//        retrofitStatic.create(RetrofitWeather.class).getWeatherReportFromDynamicUrl("weather?q="+TARGET_REQUEST+"&appid="+API_KEY).enqueue(callback);
        }
    }

    private void DynamicRequest() {
        if (binding.checkBox.isChecked()) {
            // DYNAMIC URL EXAMPLE
            // --- based on retrofit ---
            retrofitDynamic.create(RetrofitWeather.class).getWeatherReportFromDynamicUrl("http://www.faerdf.com/weather?q="+TARGET_REQUEST+"&appid="+API_KEY).enqueue(callback);
//            retrofitDynamic.create(RetrofitWeather.class).getWeatherReportFromDynamicUrl("subdomain/weather?q="+TARGET_REQUEST+"&appid="+API_KEY).enqueue(callback);

            // --- Host Selection Interceptor ---
            // NOTE: Override the Interceptor -> invalidate the functional of HttpLoggerIterceptor (I's no possible anymore log the call properly)
//        dynamicClient.setHost("url");
//        retrofitDynamic.create(RetrofitWeather.class).getWeatherReport(TARGET_REQUEST, API_KEY).enqueue(callback);
        } else {
            // --- Host Selection Interceptor ---
            // NOTE: Override the Interceptor -> invalidate the functional of HttpLoggerIterceptor (I's no possible anymore log the call properly)
//            dynamicClient.setHost("url");
            retrofitDynamic.create(RetrofitWeather.class).getWeatherReport(TARGET_REQUEST, API_KEY).enqueue(callback);

        }
    }
}
