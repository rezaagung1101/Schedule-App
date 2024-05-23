package com.powerhouse.ai.weathertraining.model.database.repository

import androidx.lifecycle.LiveData
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.utils.QueryType
import com.powerhouse.ai.weathertraining.utils.QueryUtil

class ScheduleRepository(private val database: Database) {
    private val dao = database.scheduleDao()

    fun getNearestSchedule(queryType: QueryType) : Schedule? {
        val query = QueryUtil.nearestQuery(queryType)
        return dao.getNearestSchedule(query)
    }
    fun getDetailSchedule(id: Int): Schedule = dao.getDetailSchedule(id)

    fun getTodaySchedule(day: Int): List<Schedule> = dao.getTodaySchedule(day)

    fun insertSchedule(schedule: Schedule) = dao.insertSchedule(schedule)

    fun deleteSchedule(schedule: Schedule) = dao.delete(schedule)
}