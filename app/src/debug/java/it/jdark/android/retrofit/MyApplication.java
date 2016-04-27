package it.jdark.android.retrofit;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

import it.jdark.android.retrofit.dependencyInjection.DaggerRetrofitComponent;
import it.jdark.android.retrofit.dependencyInjection.RetrofitComponent;
import it.jdark.android.retrofit.dependencyInjection.RetrofitModule;


/**
 * Created by jDark on 12/04/16.
 */
public class MyApplication extends BaseApplication {

    private final String LOG = getClass().getSimpleName();
    private final String URL = "http://api.openweathermap.org/data/2.5/";

    RetrofitComponent component;

    public RetrofitComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "Init Dagger");
        component = DaggerRetrofitComponent.builder().retrofitModule(new RetrofitModule(URL)).build();

        Log.d(LOG, "Init Debug Context");
//        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(getApplicationContext());
            AndroidDevMetrics.initWith(this);
//        }
    }
}
