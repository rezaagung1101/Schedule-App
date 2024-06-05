package com.powerhouse.ai.weathertraining.model.database.repository

import androidx.lifecycle.LiveData
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.utils.QueryType
import com.powerhouse.ai.weathertraining.utils.QueryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ScheduleRepository(private val database: Database) {
    private val dao = database.scheduleDao()

    fun getNearestSchedule(queryType: QueryType) : Schedule? {
        val query = QueryUtil.nearestQuery(queryType)
        return dao.getNearestSchedule(query)
    }
    fun getDetailSchedule(id: Int): LiveData<Schedule> = dao.getDetailSchedule(id)

    fun getScheduleByDay(day: Int): LiveData<List<Schedule>> = dao.getScheduleByDay(day)

    fun getAllScheduledDays(): LiveData<List<Int>> = dao.getAllScheduledDays()

    fun insertSchedule(schedule: Schedule) = runBlocking {
        this.launch(Dispatchers.IO) {
            dao.insertSchedule(schedule)
        }
    }

    fun deleteScheduleById(id: Int) = runBlocking {
        this.launch(Dispatchers.IO) {
            dao.deleteScheduleById(id)
        }
    }

}