package it.jdark.android.retrofit.dependencyInjection;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.jdark.android.retrofit.Utils.HostSelectionInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 21/04/16.
 *
 * @Autor jDark
 */
@Module
public class RetrofitModule {
    String baseUrl;

    public RetrofitModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    @Named("Stetho")
    Interceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }


    @Provides
    @Singleton
    @Named("HttpLog")
    Interceptor provideHttpLogInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    @Provides
    @Singleton
    HostSelectionInterceptor provideDynamicInterceptor() {
        return new HostSelectionInterceptor();
    }


    @Provides
    @Singleton
    @Named("Static")
    OkHttpClient provideStaticOkHttpClient(@Named("Stetho") Interceptor stethoInterceptor, @Named("HttpLog") Interceptor logInterceptor) {
        return new OkHttpClient().newBuilder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(logInterceptor)
                .build();
    }

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

    @Provides
    @Singleton
    @Named("Static")
    Retrofit provideStaticRetrofit(@Named("Static") OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

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
