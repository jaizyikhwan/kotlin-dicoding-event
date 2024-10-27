package com.example.fundamentalsatu.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundamentalsatu.data.response.ListEventsItem
import com.example.fundamentalsatu.databinding.ItemRowEventBinding
import com.example.fundamentalsatu.ui.detail.DetailActivity

class ListEventAdapter(private var listData: ArrayList<ListEventsItem>) : RecyclerView.Adapter<ListEventAdapter.EventViewHolder>() {

    class EventViewHolder(val binding: ItemRowEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listData.count()
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val data = listData[position]
        holder.binding.tvEventName.text = data.name
        holder.binding.tvEventSummary.text = data.summary
        Glide.with(holder.itemView.context)
            .load(data.imageLogo ?: data.mediaCover)
            .into(holder.binding.ivEventImage)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("key_event", listData[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    fun updateData(newData: ArrayList<ListEventsItem>) {
        listData = newData
        notifyDataSetChanged()
    }
}




