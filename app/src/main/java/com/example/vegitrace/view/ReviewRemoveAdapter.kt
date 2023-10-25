package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Review

class ReviewRemoveAdapter(
    private val context: Context,
    private val reviews: MutableList<Review>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ReviewRemoveAdapter.ReviewViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.reviewremove_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.farmerName.text = review.farmername
        holder.farmerEmail.text = review.email
        holder.reviewText.text = review.review

        // Set an OnClickListener for the "Remove" button
        holder.reviewremovebtn.setOnClickListener {
            showDeleteConfirmationDialog(position)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Review")
        builder.setMessage("Are you sure you want to delete this review?")

        // Add positive button for confirmation
        builder.setPositiveButton("Delete") { _, _ ->
            // User confirmed the deletion, call the onItemClick method of the listener
            listener.onItemClick(position)
        }

        // Add negative button to cancel the operation
        builder.setNegativeButton("Cancel") { _, _ ->
            // User canceled the deletion, do nothing
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Method to notify the adapter that an item has been removed
    fun itemRemovedAtUpdatedList(position: Int) {
        notifyItemRemoved(position)
    }
}
