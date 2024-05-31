package com.jetpack.compose.myweather.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.powerhouse.ai.weathertraining.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Helper {

    fun kelvinToCelcius(temp: Double): String {
        return String.format("%.1f", temp - 273.15)
    }

    fun convertTimezoneToString(timezone: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") //specifically just for timezone
        // Calculate the timezone offset in milliseconds
        val offsetMillis = timezone * 1000L
        // Get the current time in UTC and apply the offset
        val currentTimeMillis = System.currentTimeMillis() + offsetMillis
        // Convert the time to a formatted string
        return dateFormat.format(Date(currentTimeMillis))
    }

    fun getTodaysDayName(): String {
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        return dateFormat.format(Date())
    }

    fun getDayName(day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, day)
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getFormatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }

    fun getCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun showDialog(
        context: Context,
        message: String,
        negativeMsg: String,
        positiveMsg: String,
        positiveListener: () -> Unit,
    ) {
        AlertDialog.Builder(context).apply {
            setMessage(message)
            setNegativeButton(negativeMsg, null)
            setPositiveButton(positiveMsg) { _, _ ->
                positiveListener.invoke()
            }
            val dialog = this.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.danger))
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
        }
    }
}

fun currentTime(time: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(Date(time))
}
