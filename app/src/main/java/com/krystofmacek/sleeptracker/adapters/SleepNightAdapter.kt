package com.krystofmacek.sleeptracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.sleeptracker.R
import com.krystofmacek.sleeptracker.database.SleepNight
import com.krystofmacek.sleeptracker.util.convertDurationToFormatted
import com.krystofmacek.sleeptracker.util.convertNumericQualityToString

// Extending list adapter(keeps track of list behind the recyclerView) + add the constructor of SleepNightDiffCallback as argument - provides implementation getItemCount
class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        // UI elements of individual item
        private val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        private val sleepQuality: TextView = itemView.findViewById(R.id.quality_string)
        private val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        // Bind from onBindViewHolder
        fun bind(
            item: SleepNight
        ) {
            val res = itemView.context.resources
            sleepLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            sleepQuality.text = convertNumericQualityToString(item.sleepQuality, res)

            qualityImage.setImageResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_3
                }
            )
        }
        //Inflate from onCreateViewHolder
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(view)
            }
        }
    }

    // Adapter methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // getItem is provided by ListAdapter
        val item = getItem(position)
        holder.bind(item)
    }

}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}

