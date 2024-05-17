package com.powerhouse.ai.weathertraining.model.remote.api

import com.powerhouse.ai.weathertraining.model.lib.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather?")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("APPID") app_id: String?
    ): Response<Weather>

}