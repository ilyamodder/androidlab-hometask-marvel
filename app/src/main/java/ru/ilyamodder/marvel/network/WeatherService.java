package ru.ilyamodder.marvel.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ilyamodder.marvel.model.ComicsData;

/**
 * Created by ilya on 08.11.16.
 */

public interface WeatherService {
    @GET("comics")
    Call<ComicsData> getComics(@Query("dateRange") String dateRange, @Query("count") int count);
}
