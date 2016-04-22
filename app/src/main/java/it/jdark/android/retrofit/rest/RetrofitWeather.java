package it.jdark.android.retrofit.rest;

import it.jdark.android.retrofit.pojo.Model;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 12/04/16.
 * @Autor jDark
 */
public interface RetrofitWeather {

//  How the request looks like:
//  "weather?q=Rimini,it&appid=931d25ea8d06c2a5cc3bed22c2a0cdac"
    @GET("weather")
    Call<Model> getWeatherReport(@Query("q") String city, @Query("appid") String apiKey);
}
