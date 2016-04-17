package it.jdark.android.retrofit;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import it.jdark.android.retrofit.databinding.MainActivityBinding;
import it.jdark.android.retrofit.modelView.ModelView;
import it.jdark.android.retrofit.pojo.Model;
import it.jdark.android.retrofit.rest.RestInterface;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final static String LOG = "Retrofit";

    private final String URL = "http://api.openweathermap.org/data/2.5/";
    private final String API_KEY = "931d25ea8d06c2a5cc3bed22c2a0cdac";

    MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RestInterface restInterface = retrofit.create(RestInterface.class);

        // Asynchronous Request
        Call<Model> call = restInterface.getWeatherReport("Rimini,it", API_KEY);
        call.enqueue(new Callback<Model>() {
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
        });

//        // Synchronous Request without DataBinding
//        try {
//            Response<Model> response= call.execute();
//            int resCode= response.code();
//            Log.i(LOG, "Response code: " + resCode);
//            Model data= response.body();
//            city.setText("city :"+data.getName());
//            status.setText("Status :"+data.getWeather().get(0).getDescription());
//            humidity.setText("humidity :"+data.getMain().getHumidity().toString());
//            pressure.setText("pressure :"+data.getMain().getPressure().toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
