package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Order



class AddReserveAdaptor(private val context: Context,
                        private val orderList: ArrayList<Order>,
                        private val buttonClickListener: OnButtonClickListener) : RecyclerView.Adapter<AddReserveAdaptor.AddReserveViewHolder>() {


    interface OnButtonClickListener {
        fun onButtonClicked(order: Order)
    }
    inner class AddReserveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.oTitle)
        val quantity: TextView = itemView.findViewById(R.id.oSubTitle1)
        val price: TextView = itemView.findViewById(R.id.oSubTitle2)
        val resbut : Button = itemView.findViewById(R.id.resbut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddReserveViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.addreserveitem, parent, false)
        return AddReserveViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: AddReserveViewHolder, position: Int) {
        val order = orderList[position]
        holder.name.text = order.shopOwner
        holder.quantity.text = order.quantity
        holder.price.text = order.price

        holder.resbut.setOnClickListener {
            // Call the interface method to handle the button click
            buttonClickListener.onButtonClicked(order)
        }

    }

    
}
