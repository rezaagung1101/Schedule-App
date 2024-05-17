package com.powerhouse.ai.weathertraining.model.lib

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "day")
    val day: Int,

    @ColumnInfo(name = "startTime")
    val startTime: String,

    @ColumnInfo(name = "endTime")
    val endTime: String,

    @ColumnInfo(name = "note")
    val note: String
)
