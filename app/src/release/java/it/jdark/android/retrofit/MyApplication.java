package it.jdark.android.retrofit;

import android.app.Application;
import android.util.Log;

/**
 * Created by jDark on 12/04/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MainActivity.LOG, "Application - onCreate");
    }
}
