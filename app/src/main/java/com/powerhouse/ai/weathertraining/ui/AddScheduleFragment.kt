package com.powerhouse.ai.weathertraining.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.databinding.FragmentAddScheduleBinding
import com.powerhouse.ai.weathertraining.utils.TimePickerFragment

class AddScheduleFragment : Fragment(), TimePickerFragment.DialogTimeListener, TimePickerFragment.FragmentTimeListener {
    private lateinit var binding: FragmentAddScheduleBinding
    private var startTimeHour: Int = 0
    private var startTimeMinute: Int = 0
    private var endTimeHour: Int = 0
    private var endTimeMinute: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddScheduleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartTime.setOnClickListener {
            showTimePicker(it)
        }
        binding.btnEndTime.setOnClickListener {
            showTimePicker(it)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
//        Log.d("dialogTime", "Selected Date: $hour/$minute")
//        when(tag) {
//            "startTimePicker" ->{
//                startTimeHour = hour
//                startTimeMinute = minute
//                binding.tvStartTime.text = Helper.getFormatTime(startTimeHour, startTimeMinute)
//            }
//            "endTimePicker" -> {
//                endTimeHour = hour
//                endTimeMinute = minute
//                binding.tvEndTime.text = Helper.getFormatTime(endTimeHour, endTimeMinute)
//            }
//            else -> return
//        }
    }

    private fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.setListener(this)
        dialogFragment.setFragmentListener(this)
        dialogFragment.show(requireFragmentManager(), view.tag.toString())
    }

    override fun onFragmentTimeSet(tag: String?, hour: Int, minute: Int) {
        Log.d("fragmentTime", "Selected Date: $hour/$minute")
        when(tag) {
            "startTimePicker" ->{
                startTimeHour = hour
                startTimeMinute = minute
                binding.tvStartTime.text = Helper.getFormatTime(startTimeHour, startTimeMinute)
            }
            "endTimePicker" -> {
                endTimeHour = hour
                endTimeMinute = minute
                binding.tvEndTime.text = Helper.getFormatTime(endTimeHour, endTimeMinute)
            }
            else -> return
        }
    }

}