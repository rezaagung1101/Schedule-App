package com.powerhouse.ai.weathertraining.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.powerhouse.ai.weathertraining.model.database.repository.ScheduleRepository
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.utils.QueryType

class ScheduleViewModel(private val repository: ScheduleRepository) {
    private var _todaySchedule = MutableLiveData<List<Schedule>>()
    val todaySchedule: LiveData<List<Schedule>> = _todaySchedule
    private var _nearestSchedule = MutableLiveData<Schedule>()
    val nearestSchedule: LiveData<Schedule> = _nearestSchedule
    private val _queryType = MutableLiveData<QueryType>()

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun getTodaySchedule(day: Int){
        _todaySchedule.value = repository.getTodaySchedule(day)
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    fun getNearestSchedule(){
        _nearestSchedule.value = repository.getNearestSchedule(_queryType.value!!)
    }
    fun insertSchedule(schedule: Schedule) = repository.insertSchedule(schedule)

    fun deleteSchedule(schedule: Schedule) = repository.deleteSchedule(schedule)
}