package it.jdark.android.retrofit.depencencyInjection;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.jdark.android.retrofit.Utils.HostSelectionInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
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
    HostSelectionInterceptor provideDynamicInterceptor() {
        return new HostSelectionInterceptor();
    }

    @Provides
    @Singleton
    @Named("Static")
    OkHttpClient provideStaticOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .build();
    }

    @Provides
    @Singleton
    @Named("Dynamic")
    OkHttpClient provideDynamicOkHttpClient(HostSelectionInterceptor dynamicInterceptor) {
        return new OkHttpClient().newBuilder()
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
