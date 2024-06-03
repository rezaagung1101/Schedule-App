package com.powerhouse.ai.weathertraining.ui.schedule.day

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.adapter.ListScheduleAdapter
import com.powerhouse.ai.weathertraining.databinding.ActivityDetailDayBinding
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModel
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModelFactory

class DetailDayActivity : AppCompatActivity() {
    private var day: Int = 0
    private lateinit var binding: ActivityDetailDayBinding
    private lateinit var viewModel: ScheduleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = getViewModel(this)
        day = intent.getIntExtra(Constanta.day, 0)

        viewModel.getScheduleByDay(day).observe(this){
            setupInformation(it)
        }
    }

    private fun setupInformation(listSchedule: List<Schedule>) {
        binding.apply{
            tvDay.text = Helper.getDayName(day)
            val layoutManager = LinearLayoutManager(this@DetailDayActivity, LinearLayoutManager.VERTICAL, false)
            rvSchedule.layoutManager = layoutManager
            rvSchedule.adapter = ListScheduleAdapter(listSchedule)
        }
    }

    private fun getViewModel(context: Context): ScheduleViewModel {
        val viewModel: ScheduleViewModel by viewModels {
            ScheduleViewModelFactory(context)
        }
        return viewModel
    }
}