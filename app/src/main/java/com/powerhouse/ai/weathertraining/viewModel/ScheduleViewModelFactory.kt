package com.powerhouse.ai.weathertraining.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.database.repository.ScheduleRepository

class ScheduleViewModelFactory(val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScheduleViewModel::class.java)){
            val database = Database.getDatabase(context)
            return ScheduleViewModel(
                ScheduleRepository(database)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}