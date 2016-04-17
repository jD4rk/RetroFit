package it.jdark.android.retrofit.rest;

import it.jdark.android.retrofit.pojo.Model;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 12/04/16.
 * @Autor jDark
 */
public interface RestInterface {

//    @GET("weather?q=Rimini,it&appid=931d25ea8d06c2a5cc3bed22c2a0cdac")
//    Call<Model> getWeatherReport();
    @GET("weather")
    Call<Model> getWeatherReport(@Query("q") String city, @Query("appid") String apiKey);
}
