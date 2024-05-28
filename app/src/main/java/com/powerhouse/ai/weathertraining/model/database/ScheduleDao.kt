package com.powerhouse.ai.weathertraining.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.powerhouse.ai.weathertraining.model.lib.Schedule

@Dao
interface ScheduleDao {
    @RawQuery(observedEntities = [Schedule::class])
    fun getNearestSchedule(query: SupportSQLiteQuery): Schedule?

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun getDetailSchedule(id: Int): Schedule

    @Query("SELECT * FROM schedule WHERE day = :day")
    fun getScheduleByDay(day: Int): LiveData<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: Schedule)

    @Query("SELECT DISTINCT day FROM schedule ORDER BY day ASC")
    fun getAllScheduledDays(): LiveData<List<Int>>

    @Delete
    fun delete(schedule: Schedule)


}