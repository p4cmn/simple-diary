package com.artem.records.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("formattedDate")
    fun setFormattedDate(textView: TextView, timestamp: Long) {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        textView.text = format.format(date)
    }

}
