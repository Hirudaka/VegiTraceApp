package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Order
import com.google.firebase.database.*

class MyOrderAdapter(private val context: Context, private val orderList: ArrayList<Order>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val oId: TextView = itemView.findViewById(R.id.OIdtextView)
        val name: TextView = itemView.findViewById(R.id.FarmertextView)
        val vegetable: TextView = itemView.findViewById(R.id.VegtextView)
        val quantity: TextView = itemView.findViewById(R.id.QuanttextView)
        val price: TextView = itemView.findViewById(R.id.PricetextView)
        val status: TextView = itemView.findViewById(R.id.StatustextView)
        val image: ImageView = itemView.findViewById(R.id.VegimageView)
        val removebtn: ImageView = itemView.findViewById(R.id.removebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_my_order_detail, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.oId.text = order.orderId
        holder.name.text = order.shopOwner
        holder.vegetable.text = order.vegetableType
        holder.quantity.text = order.quantity
        holder.price.text = order.price
        holder.status.text = order.status

        holder.removebtn.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    fun itemRemovedAtUpdatedList(position: Int) {
        notifyItemRemoved(position)
    }
}
