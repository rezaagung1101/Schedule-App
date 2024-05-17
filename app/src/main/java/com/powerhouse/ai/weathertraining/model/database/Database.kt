package com.powerhouse.ai.weathertraining.model.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord

@androidx.room.Database(
    entities = [WeatherRecord::class, Schedule::class],
    version = 1,
    exportSchema = false
)
abstract class Database: RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        @JvmStatic
        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java, "database"
                )
                .fallbackToDestructiveMigration()
                .build()
                    .also{INSTANCE = it}
            }
        }
    }
}