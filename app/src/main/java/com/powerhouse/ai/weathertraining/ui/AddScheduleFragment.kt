package com.powerhouse.ai.weathertraining.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.R
import com.powerhouse.ai.weathertraining.databinding.FragmentAddScheduleBinding
import com.powerhouse.ai.weathertraining.utils.TimePickerFragment
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModel
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModelFactory

class AddScheduleFragment : Fragment(), TimePickerFragment.DialogTimeListener,
    TimePickerFragment.FragmentTimeListener {
    private lateinit var binding: FragmentAddScheduleBinding
    private var startTimeHour: Int = 0
    private var startTimeMinute: Int = 0
    private var endTimeHour: Int = 0
    private var endTimeMinute: Int = 0
    private lateinit var viewModel: ScheduleViewModel
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
        viewModel = getViewModel(requireContext())
        binding.btnStartTime.setOnClickListener {
            showTimePicker(it)
        }
        binding.btnEndTime.setOnClickListener {
            showTimePicker(it)
        }
        binding.btnSave.setOnClickListener {
            insertSchedule()
        }
        viewModel.isSaved.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { isSaved ->
                if(isSaved == true){
                    Toast.makeText(requireContext(), "Course saved successfully", Toast.LENGTH_SHORT).show()
                    viewModel.detailSchedule.observe(viewLifecycleOwner){ schedule ->
                        val intent = Intent(requireContext(), DetailScheduleActivity::class.java)
                        intent.putExtra(Constanta.schedule, schedule)
                        intent.putExtra(Constanta.isFromAdd, true)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to save course !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getViewModel(context: Context): ScheduleViewModel {
        val viewModel: ScheduleViewModel by viewModels {
            ScheduleViewModelFactory(context)
        }
        return viewModel
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        //nothing
    }

    private fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.setListener(this)
        dialogFragment.setFragmentListener(this)
        dialogFragment.show(requireFragmentManager(), view.tag.toString())
    }

    override fun onFragmentTimeSet(tag: String?, hour: Int, minute: Int) {
        Log.d("fragmentTime", "Selected Date: $hour/$minute")
        when (tag) {
            "startTimePicker" -> {
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

    private fun insertSchedule() {
        binding.apply {
            val scheduleName = binding.etScheduleName.text.toString()
            val day = binding.spinnerDay.selectedItemPosition
            val note = binding.etNote.text.toString()
            if (scheduleName.isEmpty() || startTimeHour == 0 || endTimeHour == 0) {
                if (scheduleName.isEmpty()) binding.etScheduleName.error =
                    "Required fields"
                Toast.makeText(requireContext(), resources.getString(R.string.input_empty_message), Toast.LENGTH_SHORT).show()
                return
            } else {
                val startTime = Helper.getFormatTime(startTimeHour, startTimeMinute)
                val endTime = Helper.getFormatTime(endTimeHour, endTimeMinute)
                viewModel.insertSchedule(scheduleName, day, startTime, endTime, note)
            }

        }
    }

}