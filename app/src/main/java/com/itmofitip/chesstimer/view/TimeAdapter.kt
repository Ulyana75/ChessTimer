package com.itmofitip.chesstimer.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.utilities.TimeItem

class TimeAdapter(
    private var dataList: List<TimeItem>,
    private val onClickListener: (item: TimeItem) -> Unit,
    private val onLongClickListener: (position: Int, view: View) -> Unit,
) : RecyclerView.Adapter<TimeAdapter.TimeItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_recycler_view_item, parent, false)
        return TimeItemHolder(view)
    }

    override fun onBindViewHolder(holder: TimeItemHolder, position: Int) {
        holder.textView.text = dataList[position].name
        holder.itemView.setOnClickListener {
            onClickListener(dataList[holder.adapterPosition])
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener(holder.adapterPosition, it)
            false
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(newData: List<TimeItem>) {
        dataList = newData
    }

    fun getData(): List<TimeItem> = dataList

    class TimeItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textView: TextView = view.findViewById(R.id.time_text)
    }
}