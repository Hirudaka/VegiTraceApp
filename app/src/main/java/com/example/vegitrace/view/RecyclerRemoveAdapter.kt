package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.WasteForm

class RecyclerRemoveAdapter(private val context: Context, private val wastages: MutableList<WasteForm>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerRemoveAdapter.WastageViewHolder>() {

    inner class WastageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.recyclernameTextView)
        val phone: TextView = itemView.findViewById(R.id.recyclerphnTextView)
        val weight: TextView = itemView.findViewById(R.id.recyclerWeightTextView)
        val date: TextView = itemView.findViewById(R.id.recyclerremoveDateTextView)
        val wastageremovebtn: ImageView = itemView.findViewById(R.id.recyclerremovebtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WastageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_remove_item, parent, false)
        return WastageViewHolder(view)
    }

    override fun onBindViewHolder(holder: WastageViewHolder, position: Int) {
        val wastage = wastages[position]
        holder.name.text = wastage.wastageName
        holder.phone.text = wastage.wastageName
        holder.weight.text = wastage.wastageWeight.toString()
        holder.date.text = wastage.wastageDate

        // Set an OnClickListener for the "Remove" button
        holder.wastageremovebtn.setOnClickListener {
            // Call the onItemClick method of the listener
            listener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return wastages.size
    }
    fun itemRemovedAtUpdatedList(position: Int) {
        notifyItemRemoved(position)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
