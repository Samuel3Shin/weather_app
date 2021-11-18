package com.example.weather

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.daily_temp_post.view.*

class DailyTempPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateText: TextView = itemView.dateTextView
    val iconImg: ImageView = itemView.iconImg
    val lowTempText: TextView = itemView.lowTempTextView
    val highTempText: TextView = itemView.highTempTextView
}