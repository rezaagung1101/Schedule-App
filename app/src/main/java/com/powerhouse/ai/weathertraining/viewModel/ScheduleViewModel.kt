package com.powerhouse.ai.weathertraining.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powerhouse.ai.weathertraining.model.database.repository.ScheduleRepository
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord
import com.powerhouse.ai.weathertraining.utils.Event
import com.powerhouse.ai.weathertraining.utils.QueryType
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    private var _todaySchedule = MutableLiveData<List<Schedule>>()
    val todaySchedule: LiveData<List<Schedule>> = _todaySchedule
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

//    fun getTodaySchedule() {
//        _todaySchedule.value = repository.getTodaySchedule()
//    }

    fun getScheduleByDay(day: Int): LiveData<List<Schedule>>{
        return repository.getScheduleByDay(day)
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    fun getNearestSchedule() {
        viewModelScope.launch {
            _nearestSchedule.value = repository.getNearestSchedule(_queryType.value!!)
        }
    }

    fun getAllScheduledDays(): LiveData<List<Int>> {
        return repository.getAllScheduledDays()
    }


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

    fun deleteSchedule(schedule: Schedule) = repository.deleteSchedule(schedule)
}