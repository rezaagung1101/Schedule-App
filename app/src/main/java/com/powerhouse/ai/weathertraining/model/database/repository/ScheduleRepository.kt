package com.powerhouse.ai.weathertraining.model.database.repository

import androidx.lifecycle.LiveData
import com.powerhouse.ai.weathertraining.model.database.Database
import com.powerhouse.ai.weathertraining.model.lib.Schedule

class ScheduleRepository(private val database: Database) {
    private val dao = database.scheduleDao()

    fun getDetailSchedule(id: Int): LiveData<Schedule> =
        dao.getDetailSchedule(id)

    fun getTodaySchedule(day: Int): List<Schedule> = dao.getTodaySchedule(day)

    fun insertSchedule(schedule: Schedule) = dao.insertSchedule(schedule)

    fun deleteSchedule(schedule: Schedule) = dao.delete(schedule)
}