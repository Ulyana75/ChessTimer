package com.itmofitip.chesstimer.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.utilities.TimeItem

class TimeAdapter(
    private val dataList: List<TimeItem>,
    private val listener: (item: TimeItem) -> Unit
) : RecyclerView.Adapter<TimeAdapter.TimeItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_recycler_view_item, parent, false)
        return TimeItemHolder(view)
    }

    override fun onBindViewHolder(holder: TimeItemHolder, position: Int) {
        holder.textView.text = dataList[position].name
        holder.itemView.setOnClickListener {
            listener(dataList[position])
        }
    }

    override fun getItemCount(): Int = dataList.size

    class TimeItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.time_text)
    }
}