package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Order

class ConfirmOrderAdapter(private val context: Context, private val orderList: ArrayList<Order>, private val listener: ConfirmOrderAdapter.OnItemClickListener) : RecyclerView.Adapter<ConfirmOrderAdapter.OrderViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val oId: TextView = itemView.findViewById(R.id.POIdtextView)
        val name: TextView = itemView.findViewById(R.id.PFarmertextView)
        val vegetable: TextView = itemView.findViewById(R.id.PVegtextView)
        val quantity: TextView = itemView.findViewById(R.id.PQuanttextView)
        val price: TextView = itemView.findViewById(R.id.PPricetextView)
        val status: TextView = itemView.findViewById(R.id.PStatustextView)
        val image: ImageView = itemView.findViewById(R.id.PVegimageView)
        val confirmbtn: Button = itemView.findViewById(R.id.confirmbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.purchase_order_details, parent, false) // Update with the correct layout
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.oId.text = order.orderId
        holder.name.text = order.farmer
        holder.vegetable.text = order.vegetableType
        holder.quantity.text = order.quantity
        holder.price.text = order.price
        holder.status.text = order.status

        holder.confirmbtn.setOnClickListener {
            listener.onItemClick(position)
        }
        val imageResId = getImageResourceForVegetable(order.vegetableType)
        holder.image.setImageResource(imageResId)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun itemRemovedAtUpdatedList(position: Int) {
// Remove the item from the list
        notifyItemRemoved(position)
    }

    private fun getImageResourceForVegetable(vegetableName: String): Int {
        // Define a mapping of vegetable names to image resource IDs
        val vegetableImageMap = mapOf(
            "Carrot" to R.drawable.carrots,
            "Beans" to R.drawable.greenbeans,
            "Cabbage" to R.drawable.cabbage,
            "Egg Plant" to R.drawable.eggplant,
            "BeetRoot" to R.drawable.beet,
            "Corn" to R.drawable.corn,

        )

        // Look up the image resource ID for the given vegetable name
        return vegetableImageMap[vegetableName] ?: 0
    }
}
