package it.jdark.android.retrofit;

import android.app.Application;
import android.util.Log;


/**
 * Created by jDark on 12/04/16.
 */
public class BaseApplication extends Application {

    private final String LOG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "onCreate");
    }
}
