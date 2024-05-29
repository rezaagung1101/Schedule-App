package com.powerhouse.ai.weathertraining.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.jetpack.compose.myweather.utils.Constanta
import com.powerhouse.ai.weathertraining.R
import com.powerhouse.ai.weathertraining.databinding.ActivityDetailScheduleBinding
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModel
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModelFactory

class DetailScheduleActivity : AppCompatActivity() {
    private var schedule =
        Schedule(day = 1, endTime = "00:00", startTime = "00.00", scheduleName = "", note = "")
    private var isFromAdd: Boolean = false
    private lateinit var binding: ActivityDetailScheduleBinding
    private lateinit var viewModel: ScheduleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.detail_schedule)
        viewModel = getViewModel(this)
        schedule = intent.getParcelableExtra<Schedule>(Constanta.schedule) as Schedule
        isFromAdd = intent.getBooleanExtra(Constanta.isFromAdd, false)
        setupInformation(schedule)
    }

    private fun setupInformation(schedule: Schedule) {
        binding.apply {
            tvScheduleName.text = schedule.scheduleName
            tvTime.text = "${schedule.startTime}-${schedule.endTime}"
            tvNote.text = schedule.note
        }

    }

    override fun onBackPressed() {
        if (isFromAdd) {
            super.onBackPressed()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(getString(R.string.delete_alert))
                    setNegativeButton(getString(R.string.no), null)
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deleteSchedule(schedule)
                        Toast.makeText(this@DetailScheduleActivity, resources.getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getViewModel(context: Context): ScheduleViewModel {
        val viewModel: ScheduleViewModel by viewModels {
            ScheduleViewModelFactory(context)
        }
        return viewModel
    }
}