package com.sevenexp.craftit.widget

import android.content.Context
import com.sevenexp.craftit.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Helper {
    private const val timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    fun getCurrentDate(): Date {
        return Date()
    }

    private fun parseUTCDate(timestamp: String): Date {
        return try {
            val formatter = SimpleDateFormat(timestampFormat, Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.parse(timestamp) as Date
        } catch (e: ParseException) {
            getCurrentDate()
        }
    }

    fun getTimelineUpload(context: Context, timestamp: String): String {
        val currentTime = getCurrentDate()
        val uploadTime = parseUTCDate(timestamp)
        val diff: Long = currentTime.time - uploadTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val label = when (minutes.toInt()) {
            0 -> "$seconds ${context.getString(R.string.text_seconds_ago)}"
            in 1..59 -> "$minutes ${context.getString(R.string.text_minutes_ago)}"
            in 60..1440 -> "$hours ${context.getString(R.string.text_hours_ago)}"
            else -> "$days ${context.getString(R.string.text_days_ago)}"
        }
        return label
    }
}