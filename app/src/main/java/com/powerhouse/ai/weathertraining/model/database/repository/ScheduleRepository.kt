package com.powerhouse.ai.weathertraining.model.database.repository

import androidx.lifecycle.LiveData
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord
import com.powerhouse.ai.weathertraining.utils.QueryType
import com.powerhouse.ai.weathertraining.utils.QueryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class ScheduleRepository(private val database: Database) {
    private val dao = database.scheduleDao()

    fun getNearestSchedule(queryType: QueryType) : Schedule? {
        val query = QueryUtil.nearestQuery(queryType)
        return dao.getNearestSchedule(query)
    }
    fun getDetailSchedule(id: Int): Schedule = dao.getDetailSchedule(id)

    fun getScheduleByDay(day: Int): List<Schedule> = dao.getScheduleByDay(day)

    fun getTodaySchedule() : List<Schedule> {
        val currentDay = Helper.getCurrentDay()
        return dao.getScheduleByDay(currentDay)
    }

    fun insertSchedule(schedule: Schedule) = runBlocking {
        this.launch(Dispatchers.IO) {
            dao.insertSchedule(schedule)
        }
    }
    fun deleteSchedule(schedule: Schedule) = runBlocking {
        this.launch(Dispatchers.IO) {
            dao.delete(schedule)
        }
    }
}