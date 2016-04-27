package it.jdark.android.retrofit.dependencyInjection;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
    @Named("stetho")
    Interceptor provideInterceptor() {
        return new StethoInterceptor();
    }


    @Provides
    @Singleton
    @Named("log")
    Interceptor proviteInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(@Named("stetho") Interceptor stethoInterceptor, @Named("log") Interceptor logInterceptor) {
        return new OkHttpClient().newBuilder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(logInterceptor).build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
