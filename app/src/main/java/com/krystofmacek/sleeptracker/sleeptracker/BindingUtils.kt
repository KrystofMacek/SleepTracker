package com.krystofmacek.sleeptracker.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.krystofmacek.sleeptracker.R
import com.krystofmacek.sleeptracker.database.SleepNight
import com.krystofmacek.sleeptracker.util.convertDurationToFormatted
import com.krystofmacek.sleeptracker.util.convertNumericQualityToString

@BindingAdapter("SleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight) {
    text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
}

@BindingAdapter("SleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight) {
    text = convertNumericQualityToString(item.sleepQuality, context.resources)
}

@BindingAdapter("SleepImage")
fun ImageView.setSleepImage(item: SleepNight) {
    setImageResource(when (item.sleepQuality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_3
    })
}