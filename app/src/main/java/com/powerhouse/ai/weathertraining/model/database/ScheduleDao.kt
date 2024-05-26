package com.powerhouse.ai.weathertraining.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.powerhouse.ai.weathertraining.model.lib.Schedule

@Dao
interface ScheduleDao{
    @RawQuery(observedEntities = [Schedule::class])
    fun getNearestSchedule(query: SupportSQLiteQuery): Schedule?

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun getDetailSchedule(id: Int): Schedule

    @Query("SELECT * FROM schedule WHERE day = :day")
    fun getScheduleByDay(day: Int): List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)


}