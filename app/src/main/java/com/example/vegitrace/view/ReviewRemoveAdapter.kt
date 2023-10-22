package com.example.vegitrace.view

import Review
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R

class ReviewRemoveAdapter(private val context: Context, private val reviews: List<Review>) : RecyclerView.Adapter<ReviewRemoveAdapter.ReviewViewHolder>() {
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val farmerName: TextView = itemView.findViewById(R.id.farmerNameremoveTextView)
        val farmerEmail: TextView = itemView.findViewById(R.id.farmerEmailremoveTextView)
        val reviewText: TextView = itemView.findViewById(R.id.reviewremoveTextView)
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
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}
