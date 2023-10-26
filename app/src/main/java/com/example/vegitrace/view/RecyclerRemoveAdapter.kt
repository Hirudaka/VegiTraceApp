package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.WasteForm

class RecyclerRemoveAdapter(
    private val context: Context,
    private val wastages: MutableList<WasteForm>,
    private val listener: OnItemClickListener,
    private val currentUserID: String
) : RecyclerView.Adapter<RecyclerRemoveAdapter.WastageViewHolder>() {

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

        if (wastage.currentUserId == currentUserID) {
            holder.name.text = wastage.wastageName
            holder.phone.text = wastage.wastagePhone
            holder.weight.text = wastage.wastageWeight.toString()
            holder.date.text = wastage.wastageDate
        } else {
            holder.name.text = wastage.wastageName
            holder.phone.text = "*Hidden Info*"
            holder.weight.text = "*Hidden Info*"
            holder.date.text = "*Hidden Info*"

            holder.wastageremovebtn.visibility = View.INVISIBLE
            holder.wastageremovebtn.isClickable = false
        }

        holder.wastageremovebtn.setOnClickListener {
            // Call the onItemClick method of the listener
            showConfirmationDialog(position)
            Toast.makeText(holder.itemView.context, "Your Reservation is Deleted!!", Toast.LENGTH_LONG).show()
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

    private fun showConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Order")
        builder.setMessage("Are you sure you want to Remove? this action cannot be revert. ")

        // Add positive button for confirmation
        builder.setPositiveButton("Confirm") { _, _ ->
            listener.onItemClick(position)
            // Perform the confirmation action here
        }

        // Add negative button to cancel the operation
        builder.setNegativeButton("Cancel") { _, _ ->
            // User canceled the confirmation, do nothing
        }
    }
}
