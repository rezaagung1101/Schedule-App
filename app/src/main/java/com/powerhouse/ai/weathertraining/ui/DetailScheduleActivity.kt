package com.powerhouse.ai.weathertraining.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jetpack.compose.myweather.utils.Constanta
import com.powerhouse.ai.weathertraining.databinding.ActivityDetailScheduleBinding
import com.powerhouse.ai.weathertraining.model.lib.Schedule

class DetailScheduleActivity : AppCompatActivity() {
    private var schedule = Schedule(day = 1, endTime = "00:00", startTime = "00.00", scheduleName = "", note = "")
    private lateinit var binding: ActivityDetailScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        schedule = intent.getParcelableExtra<Schedule>(Constanta.schedule) as Schedule
        setupInformation(schedule)
    }

    private fun setupInformation(schedule: Schedule){
        binding.tvScheduleName.text = schedule.scheduleName
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}