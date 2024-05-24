package com.powerhouse.ai.weathertraining.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.powerhouse.ai.weathertraining.R
import com.powerhouse.ai.weathertraining.databinding.ActivityMainBinding
import com.powerhouse.ai.weathertraining.utils.TimePickerFragment

class MainActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.title = ""
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_container)

        //Untuk mengatur title di action bar
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home,
//                R.id.navigation_add_schedule,
//                R.id.navigation_list_day_schedule,
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        Log.d("MainActivity_$tag", "Selected Date: $hour/$minute")
    }


}