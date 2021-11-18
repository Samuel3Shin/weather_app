package com.example.weather

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*

class DailyTempPostAdapter(private val context: Context, private val posts: MutableList<DailyTempPost>): RecyclerView.Adapter<DailyTempPostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTempPostViewHolder {
        Log.d("TAG", "Adapter onCreateViewHolder")
        return DailyTempPostViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.daily_temp_post, parent, false))
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "Adapter post size: " + posts.size.toString())
        return posts.size
    }

    override fun onBindViewHolder(holder: DailyTempPostViewHolder, position: Int) {
        Log.d("TAG", "Adapter binding")
        val post = posts[position]
        holder.dateText.text = post.date
        holder.iconImg.setImageResource(R.drawable.ic_clear_day);
        holder.lowTempText.text = post.lowTemp
        holder.highTempText.text = post.highTemp

//        holder.itemView.setOnClickListener {
//            val intent = Intent(context.applicationContext, DetailActivity::class.java)
//            val boardKey = boardKeys[position]
//
//            intent.putExtra("boardKey", boardKey)
//            intent.putExtra("postId", post.postId)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            context.startActivity(intent)
//
//        }

    }
}