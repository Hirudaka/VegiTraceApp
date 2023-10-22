package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Order

class OrderAdapter(private val context: Context, private val orderList: ArrayList<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {




    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.oTitle)
        val quantity: TextView = itemView.findViewById(R.id.oSubTitle1)
        val price: TextView = itemView.findViewById(R.id.oSubTitle2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.order_item, parent, false)




        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.name.text = order.shopOwner
        holder.quantity.text = order.quantity
        holder.price.text = order.price
    }
}
