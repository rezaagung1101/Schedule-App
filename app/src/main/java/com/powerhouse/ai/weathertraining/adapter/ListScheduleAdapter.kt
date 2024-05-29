package com.powerhouse.ai.weathertraining.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.databinding.CardScheduleItemBinding
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.ui.DetailScheduleActivity
import com.powerhouse.ai.weathertraining.ui.schedule.DetailDayActivity

class ListScheduleAdapter(private val listData: List<Schedule>) :
    RecyclerView.Adapter<ListScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CardScheduleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = listData[position]
        holder.binding.apply {
            tvTime.text = schedule.startTime
            tvScheduleName.text = schedule.scheduleName
            root.setOnClickListener {
                val intent = Intent(it.context, DetailScheduleActivity::class.java)
                intent.putExtra(Constanta.schedule, schedule)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = listData.size
}
