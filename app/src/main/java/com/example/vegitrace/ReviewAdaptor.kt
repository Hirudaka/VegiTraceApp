package com.example.vegitrace

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Review

class ReviewAdaptor(private val revList : ArrayList<Review>): RecyclerView.Adapter<ReviewAdaptor.ReviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {



        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reviewitem,parent,false)
        return ReviewViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return revList.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {



        val currentItem = revList[position]

        holder.shopNum.text = currentItem.ownername
        holder.Owner.text = currentItem.email
        holder.review.text = currentItem.review

    }

    class ReviewViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        val shopNum : TextView = itemView.findViewById(R.id.shopno)
        val Owner : TextView = itemView.findViewById(R.id.owner)
        val review : TextView = itemView.findViewById(R.id.review)

    }

}