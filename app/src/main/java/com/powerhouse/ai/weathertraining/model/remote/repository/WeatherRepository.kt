package com.powerhouse.ai.weathertraining.model.remote.repository

import androidx.lifecycle.LiveData
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord
import com.powerhouse.ai.weathertraining.model.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WeatherRepository(private val database: Database, private val apiService: ApiService) {
    suspend fun getCurrentWeather(lat: String, long: String, app_id: String) =
        apiService.getCurrentWeather(lat, long, app_id)

    fun insertWeather(weather: WeatherRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            database.weatherDao().insertWeather(weather)
        }
    }

    fun getCityWeather(city: String): LiveData<WeatherRecord> {
        return database.weatherDao().getCityWeather(city)
    }
}