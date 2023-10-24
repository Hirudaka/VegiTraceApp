package com.example.vegitrace.view



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Review


class ReviewAdapter(private val context: Context, private var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val farmerName: TextView = itemView.findViewById(R.id.farmerNameTextView)
        val shopno: TextView = itemView.findViewById(R.id.shopNoTextView)
        val ownerEmail: TextView = itemView.findViewById(R.id.shopOwnerEmailTextView)
        val farmerEmail: TextView = itemView.findViewById(R.id.farmerEmailTextView)
        val reviewText: TextView = itemView.findViewById(R.id.reviewTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.shop_review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.farmerName.text = review.farmername
        holder.ownerEmail.text = review.owneremail
        holder.shopno.text = review.shopno
        holder.farmerEmail.text = review.email
        holder.reviewText.text = review.review
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
    fun updateReviews(filteredReviews: List<Review>) {
        reviews = filteredReviews
        notifyDataSetChanged()
    }

}
