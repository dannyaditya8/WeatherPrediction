package com.kota205.dannyaditya.weatherprediction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("forecast/ba30809c3e19a11c9fdc2c04510d5511/{latlong}")
    Call<Weather> getInfo(@Path("latlong") String latlong);
}
