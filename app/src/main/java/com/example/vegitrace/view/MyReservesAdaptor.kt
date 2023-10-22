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



class MyReserveAdaptor(private val context: Context,
                        private val reservedList: ArrayList<Order>,
                        private val buttonClickListener: OnButtonClickListener) : RecyclerView.Adapter<MyReserveAdaptor.MyReserveViewHolder>() {


    interface OnButtonClickListener {
        fun onButtonClicked(order: Order)
    }
    inner class MyReserveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val owner : TextView = itemView.findViewById(R.id.owner)
        val veg : TextView = itemView.findViewById(R.id.veg)
        val price : TextView = itemView.findViewById(R.id.price)
        val qty : TextView = itemView.findViewById(R.id.quantity)
        val status : TextView = itemView.findViewById(R.id.stat)
        val removebtn : Button = itemView.findViewById(R.id.remove)
        val image: ImageView = itemView.findViewById(R.id.imageView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReserveViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.reserveitem, parent, false)
        return MyReserveViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reservedList.size
    }

    override fun onBindViewHolder(holder: MyReserveViewHolder, position: Int) {
        val rev = reservedList[position]
        holder.owner.text = rev.supplier
        holder.veg.text = rev.vegetableType
        holder.price.text = rev.price
        holder.qty.text = rev.quantity
        holder.status.text = rev.status
        val imageResId = getImageResourceForVegetable(rev.vegetableType)
        holder.image.setImageResource(imageResId)


        holder.removebtn.setOnClickListener {
            // Call the interface method to handle the button click
            buttonClickListener.onButtonClicked(rev)
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
