package com.powerhouse.ai.weathertraining.ui.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.powerhouse.ai.weathertraining.adapter.ListDayAdapter
import com.powerhouse.ai.weathertraining.databinding.FragmentListScheduledDaysBinding
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModel
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModelFactory

class ListScheduledDaysFragment : Fragment() {
    private lateinit var binding: FragmentListScheduledDaysBinding
    private lateinit var viewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListScheduledDaysBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel(requireContext())
        viewModel.getAllScheduledDays().observe(viewLifecycleOwner){
            setupInformation(it)
        }

    }

    private fun setupInformation(scheduledDays: List<Int>){
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apply {
            rvDays.layoutManager = layoutManager
            rvDays.adapter = ListDayAdapter(scheduledDays)
        }
    }

    private fun getViewModel(context: Context): ScheduleViewModel {
        val viewModel: ScheduleViewModel by viewModels {
            ScheduleViewModelFactory(context)
        }
        return viewModel
    }

}