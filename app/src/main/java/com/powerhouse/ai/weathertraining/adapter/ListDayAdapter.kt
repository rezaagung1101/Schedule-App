package com.powerhouse.ai.weathertraining.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.databinding.CardDayItemBinding
import com.powerhouse.ai.weathertraining.ui.schedule.day.DetailDayActivity

class ListDayAdapter(private val listData: List<Int>) :
    RecyclerView.Adapter<ListDayAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CardDayItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = listData[position]
        holder.binding.apply {
            tvDay.text = Helper.getDayName(day)
            tvDayCount.text = Helper.dayCountFormat(day)
            root.setOnClickListener {
                val intent = Intent(it.context, DetailDayActivity::class.java)
                intent.putExtra(Constanta.day, day)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = listData.size
}
