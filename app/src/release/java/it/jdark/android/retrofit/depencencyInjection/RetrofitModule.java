package it.jdark.android.retrofit.depencencyInjection;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.jdark.android.retrofit.utils.HostSelectionInterceptor;
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

    /**
     * Dagger Module to inject {@link it.jdark.android.retrofit.utils HostSelectionInterceptor} into a {@link okhttp3 OkHttpClient} that allow to change the url between two different request
     * @return HostSelectionInterceptor instance object
     */
    @Provides
    @Singleton
    HostSelectionInterceptor provideDynamicInterceptor() {
        return new HostSelectionInterceptor();
    }

    /**
     * Dagger Module to inject a {@link okhttp3 OkHttpClient} for static request only
     * @return OkHttpClient instance object
     */
    // OkHttpClient for static request
    @Provides
    @Singleton
    @Named("Static")
    OkHttpClient provideStaticOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .build();
    }

    /**
     * Dagger Module to inject a {@link okhttp3 OkHttpClient} that allow to make dynamic request changing the url by use of
     * {@link it.jdark.android.retrofit.utils HostSelectionInterceptor}
     *
     * @param dynamicInterceptor HostSelectionInterceptor
     *
     * @return OkHttpClient instance object
     */
    @Provides
    @Singleton
    @Named("Dynamic")
    OkHttpClient provideDynamicOkHttpClient(HostSelectionInterceptor dynamicInterceptor) {
        return new OkHttpClient().newBuilder()
                .addInterceptor(dynamicInterceptor)
                .build();
    }

    /**
     * Dagger Module to inject {@link retrofit2 Retrofit} object with
     * {@link okhttp3 OkHttpClient} for <u>static request only!</u>
     *
     * @param okHttpClient OkHttpClient(Static)
     *
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
     * Dagger Module to inject {@link retrofit2 Retrofit} object with {@link okhttp3 OkHttpClient} for dynamic request
     * <br> (it has inside {@link it.jdark.android.retrofit.utils HostSelectionInterceptor} that allow to change and set the "url" of the call)
     *
     * @param okHttpClient OkHttpClient(Dynamic)
     *
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
