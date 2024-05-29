package com.powerhouse.ai.weathertraining.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.model.database.repository.ScheduleRepository
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord
import com.powerhouse.ai.weathertraining.utils.Event
import com.powerhouse.ai.weathertraining.utils.QueryType
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
//    private var _schedule = MutableLiveData<Schedule>()
//    val schedule: LiveData<Schedule> = _schedule
    private var _nearestSchedule = MutableLiveData<Schedule>()
    val nearestSchedule: LiveData<Schedule> = _nearestSchedule
    private var _detailSchedule = MutableLiveData<Schedule>()
    val detailSchedule: LiveData<Schedule> = _detailSchedule
    private val _queryType = MutableLiveData<QueryType>()
    private var _isSaved = MutableLiveData<Event<Boolean>>()
    val isSaved: LiveData<Event<Boolean>> = _isSaved

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun getTodaySchedule(): LiveData<List<Schedule>> {
        val currentDay = Helper.getCurrentDay()
        return repository.getScheduleByDay(currentDay)
    }

    fun getScheduleByDay(day: Int): LiveData<List<Schedule>> = repository.getScheduleByDay(day)


    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    fun getNearestSchedule() {
        viewModelScope.launch {
            _nearestSchedule.value = repository.getNearestSchedule(_queryType.value!!)
        }
    }

    fun getAllScheduledDays(): LiveData<List<Int>> = repository.getAllScheduledDays()

    fun insertSchedule(
        scheduleName: String,
        day: Int,
        startTime: String,
        endTime: String,
        note: String,
    ) {
        if (scheduleName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            _isSaved.value = Event(false)
            return
        }

        val schedule = Schedule(
            scheduleName = scheduleName,
            day = day + 1,
            startTime = startTime,
            endTime = endTime,
            note = note
        )
        repository.insertSchedule(schedule)
        _isSaved.value = Event(true)
        _detailSchedule.value = schedule
    }

    fun deleteSchedule(schedule: Schedule) = repository.deleteScheduleById(schedule.id)

}