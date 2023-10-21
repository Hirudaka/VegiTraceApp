package com.example.vegitrace.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Order
import com.google.firebase.database.*

class MyOrderAdapter(private val context: Context, private val orderList: ArrayList<Order>) : RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>() {

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

        // Add a click listener to the "Remove" button
        holder.removebtn.setOnClickListener {
            // Retrieve the order's unique identifier (orderId)
            val orderId = order.orderId

            // Remove the order from the database
            removeOrderFromDatabase(orderId)
        }

        // Map vegetable names to image resources in your app
        val imageResId = getImageResourceForVegetable(order.vegetableType)
        holder.image.setImageResource(imageResId)
    }

    // Function to remove an order from the database
    private fun removeOrderFromDatabase(orderId: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")
        val orderRef = databaseReference.child(orderId)

        orderRef.removeValue()

            .addOnSuccessListener {
                // Order removed successfully
                Log.d("RemoveOrder", "Order removed successfully")
                Toast.makeText(context, "Order removed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle the error when the order removal fails
                Log.e("RemoveOrder", "Error removing order: $e")
                Toast.makeText(context, "Error removing order. Please try again.", Toast.LENGTH_SHORT).show()
            }
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
