package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Review

class ReviewRemoveAdapter(private val context: Context, private val reviews: MutableList<Review>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ReviewRemoveAdapter.ReviewViewHolder>() {

    // Listener interface for item clicks
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val farmerName: TextView = itemView.findViewById(R.id.farmerNameremoveTextView)
        val farmerEmail: TextView = itemView.findViewById(R.id.farmerEmailremoveTextView)
        val reviewText: TextView = itemView.findViewById(R.id.reviewremoveTextView)
        val reviewremovebtn: ImageView = itemView.findViewById(R.id.reviewremovebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewRemoveAdapter.ReviewViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.reviewremove_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewRemoveAdapter.ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.farmerName.text = review.farmername
        holder.farmerEmail.text = review.email
        holder.reviewText.text = review.review

        // Set an OnClickListener for the "Remove" button
        holder.reviewremovebtn.setOnClickListener {
            // Call the onItemClick method of the listener
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    // Method to notify the adapter that an item has been removed
    fun itemRemovedAtUpdatedList(position: Int) {
        notifyItemRemoved(position)
    }
}
