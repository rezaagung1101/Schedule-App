package com.powerhouse.ai.weathertraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.powerhouse.ai.weathertraining.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}