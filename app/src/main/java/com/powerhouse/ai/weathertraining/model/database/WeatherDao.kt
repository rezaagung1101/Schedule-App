package com.powerhouse.ai.weathertraining.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord

@Dao
interface WeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherRecord: WeatherRecord)

    @Query("SELECT * FROM weather WHERE city = :city")
    fun getCityWeather(city: String): LiveData<WeatherRecord>
}