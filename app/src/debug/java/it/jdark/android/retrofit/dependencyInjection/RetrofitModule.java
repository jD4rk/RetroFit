package it.jdark.android.retrofit.dependencyInjection;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.jdark.android.retrofit.utils.HostSelectionInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 21/04/16.
 * @Autor jDark
 */
@Module
public class RetrofitModule {
    String baseUrl;

    public RetrofitModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Dagger Module to inject {@link okhttp3 Interceptor} with "Stetho" logging function (chrome)
     *
     * @return
     */
    @Provides
    @Singleton
    @Named("Stetho")
    Interceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    /**
     * Dagger Module to inject {@link okhttp3 Interceptor} with logging enable via android monitor
     *
     * @return Interceptor object
     */
    @Provides
    @Singleton
    @Named("HttpLog")
    Interceptor provideHttpLogInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * Dagger Module to inject {@link it.jdark.android.retrofit.utils HostSelectionInterceptor to make dynamic request (by change the url)
     *
     * @return HttpSelectionInterceptor object
     */
    @Provides
    @Singleton
    HostSelectionInterceptor provideDynamicInterceptor() {
        return new HostSelectionInterceptor();
    }


    /** Dagger Module to inject a {@link okhttp3 OkHttpClient} with both "stetho", "httpLog" to make static request
     *
     * @param stethoInterceptor interceptor(stetho)
     * @param logInterceptor interceptor(httplog)
     * @return OkHttpClient
     */
    @Provides
    @Singleton
    @Named("Static")
    OkHttpClient provideStaticOkHttpClient(@Named("Stetho") Interceptor stethoInterceptor, @Named("HttpLog") Interceptor logInterceptor) {
        return new OkHttpClient().newBuilder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(logInterceptor)
                .build();
    }

    /**
     *  Dagger Module to inject a {@link okhttp3 OkHttpClient} with both interceptor: "stetho", "httpLog" for logging.
     *  <br>Plus interceptor {@link it.jdark.android.retrofit.utils HttpSelectionInterceptor} to allow change the "url" at runtime
     *
     * @param dynamicInterceptor HostSelectionInterceptor
     * @param stethoInterceptor interceptor(stetho)
     * @param logInterceptor interceptor(HttpLog)
     * @return {@link okhttp3 OkHttpClient}
     */
    @Provides
    @Singleton
    @Named("Dynamic")
    OkHttpClient provideDynamicOkHttpClient(HostSelectionInterceptor dynamicInterceptor, @Named("Stetho") Interceptor stethoInterceptor, @Named("HttpLog") Interceptor logInterceptor) {
        return new OkHttpClient().newBuilder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(logInterceptor)
                .addInterceptor(dynamicInterceptor)
                .build();
    }

    /**
     * Dagger Module to inject {@link retrofit2 Retrofit} object object with <b>Static HttpClient</b>
     * <br> It can be used to make to make <u>static request only</u>
     *
     * @param okHttpClient HttpClient(static)
     * @return Retrofit instance object
     */
    @Provides
    @Singleton
    @Named("Static")
    Retrofit provideStaticRetrofit(@Named("Static") OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * Dagger Module to inject {@link Retrofit Retrofit} object with <b>Dynamic HttpClient</b>.
     * <br>It can be used to make Dynamic request by change the "url" by call <b>setHost</b> on {@link it.jdark.android.retrofit.utils HostSelectionInterceptor} object.
     * @param okHttpClient HttpClient(Dynamic)
     * @return Retrofit instance object
     */
    @Provides
    @Singleton
    @Named("Dynamic")
    Retrofit provideDynamicRetrofit(@Named("Dynamic") OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .callFactory(okHttpClient)
                .build();
    }
}
