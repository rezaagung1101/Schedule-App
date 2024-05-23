package com.powerhouse.ai.weathertraining.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.powerhouse.ai.weathertraining.R
import com.powerhouse.ai.weathertraining.databinding.ActivityAddScheduleBinding

class AddScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}