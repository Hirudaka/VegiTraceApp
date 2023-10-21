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

class MyOrderAdapter(private val context: Context, private val orderList: ArrayList<Order>) : RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val oId: TextView = itemView.findViewById(R.id.OIdtextView)
        val name: TextView = itemView.findViewById(R.id.FarmertextView)
        val vegetable: TextView = itemView.findViewById(R.id.VegtextView)
        val quantity: TextView = itemView.findViewById(R.id.QuanttextView)
        val price: TextView = itemView.findViewById(R.id.PricetextView)
        val status: TextView = itemView.findViewById(R.id.StatustextView)
        val image: ImageView = itemView.findViewById(R.id.VegimageView)
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
        holder.name.text = order.supplier
        holder.vegetable.text = order.vegetableType
        holder.quantity.text = order.quantity
        holder.price.text = order.price
        holder.status.text = order.status

        // Map vegetable names to image resources in your app
        val imageResId = getImageResourceForVegetable(order.vegetableType)
        holder.image.setImageResource(imageResId)
    }

    private fun getImageResourceForVegetable(vegetableName: String): Int {
        // Define a mapping of vegetable names to image resource IDs
        val vegetableImageMap = mapOf(
            "Carrot" to R.drawable.carrots,
            "Beans" to R.drawable.greenbeans,
            "Cabbage" to R.drawable.cabbage,
            // Add more vegetable-to-image mappings as needed
        )

        // Look up the image resource ID for the given vegetable name
        return vegetableImageMap[vegetableName] ?: 0
    }
}
