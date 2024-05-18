package com.powerhouse.ai.weathertraining.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.remote.api.ApiService
import com.powerhouse.ai.weathertraining.model.remote.repository.WeatherRepository

class WeatherViewModelFactory(val context: Context, private val apiService: ApiService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            val database = Database.getDatabase(context)
            return WeatherViewModel(
                WeatherRepository(database, apiService)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}