package com.powerhouse.ai.weathertraining.utils

import android.content.Context
import android.content.SharedPreferences
import com.jetpack.compose.myweather.utils.Constanta


class UserPreference(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constanta.setting, Context.MODE_PRIVATE)
    private val preferences: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveCurrentCity(city: String){
        preferences.putString(Constanta.city, city)
        preferences.apply()
    }
    fun getCurrentCity(): String? {
        return sharedPreferences.getString(Constanta.city, null)
    }
}
