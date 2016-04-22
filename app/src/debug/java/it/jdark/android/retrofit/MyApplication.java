package it.jdark.android.retrofit;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

import it.jdark.android.retrofit.dependencyInjection.DaggerRetrofitComponent;
import it.jdark.android.retrofit.dependencyInjection.RetrofitComponent;
import it.jdark.android.retrofit.dependencyInjection.RetrofitModule;


/**
 * Created by jDark on 12/04/16.
 */
public class MyApplication extends Application {

    private final String LOG = getClass().getSimpleName();
    private final String URL = "http://api.openweathermap.org/data/2.5/";

    RetrofitComponent component;

    public RetrofitComponent getComponent() {
        return component;
    }


    @Override
    public void onCreate() {
        Log.d(LOG, "onCreate");
        super.onCreate();

        component = DaggerRetrofitComponent.builder().retrofitModule(new RetrofitModule(URL)).build();
        Stetho.initializeWithDefaults(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }

    }
}
