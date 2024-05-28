package com.powerhouse.ai.weathertraining.ui.schedule

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.databinding.ActivityDetailDayBinding
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
        setupInformation(day)
    }

    private fun setupInformation(day: Int) {
        binding.apply{
            binding.tvDay.text = Helper.getDayName(day)
            viewModel.getScheduleByDay(day)
        }
    }

    private fun getViewModel(context: Context): ScheduleViewModel {
        val viewModel: ScheduleViewModel by viewModels {
            ScheduleViewModelFactory(context)
        }
        return viewModel
    }
}