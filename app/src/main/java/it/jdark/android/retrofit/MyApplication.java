package it.jdark.android.retrofit;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;

/**
 * Created by jDark on 12/04/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MainActivity.LOG, "Application - onCreate");
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
        Stetho.initializeWithDefaults(getApplicationContext());

    }
}
