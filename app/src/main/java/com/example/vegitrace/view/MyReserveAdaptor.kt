package com.example.vegitrace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.R
import com.example.vegitrace.model.Order



class MyReserveAdaptor(private val context: Context,
                        private val reservedList: ArrayList<Order>,
                        private val buttonClickListener: OnButtonClickListener) : RecyclerView.Adapter<MyReserveAdaptor.MyReserveViewHolder>() {


    interface OnButtonClickListener {
        fun onButtonClicked(position: Int)
    }
    inner class MyReserveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id : TextView = itemView.findViewById(R.id.OIdtext)
        val owner : TextView = itemView.findViewById(R.id.Ownertext)
        val veg : TextView = itemView.findViewById(R.id.Vegtext)
        val price : TextView = itemView.findViewById(R.id.Pricetext)
        val qty : TextView = itemView.findViewById(R.id.Quanttext)
        val status : TextView = itemView.findViewById(R.id.Statustext)
        val removebtn : Button = itemView.findViewById(R.id.removebtn)
        val image: ImageView = itemView.findViewById(R.id.Vegimage)


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
        holder.id.text = rev.orderId
        holder.owner.text = rev.shopOwner
        holder.veg.text = rev.vegetableType
        holder.price.text = rev.price
        holder.qty.text = rev.quantity
        holder.status.text = rev.status
        val imageResId = getImageResourceForVegetable(rev.vegetableType)
        holder.image.setImageResource(imageResId)


        holder.removebtn.setOnClickListener {
            // Call the interface method to handle the button click
            showConfirmationDialog(position)
        }

    }

    private fun getImageResourceForVegetable(vegetableName: String): Int {
        // Define a mapping of vegetable names to image resource IDs
        val vegetableImageMap = mapOf(
            "Carrot" to R.drawable.carrots,
            "Beans" to R.drawable.greenbeans,
            "Cabbage" to R.drawable.cabbage,
            "EggPlant" to R.drawable.eggplant,
            "BeetRoot" to R.drawable.beet,
            "Corn" to R.drawable.corn,
            // Add more vegetable-to-image mappings as needed
        )

        // Look up the image resource ID for the given vegetable name
        return vegetableImageMap[vegetableName] ?: 0
    }
    private fun showConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Order")
        builder.setMessage("Are you sure you want to remove the Order? this action cannot be revert. ")

        // Add positive button for confirmation
        builder.setPositiveButton("Confirm") { _, _ ->
            buttonClickListener.onButtonClicked(position)
            // Perform the confirmation action here
        }

        // Add negative button to cancel the operation
        builder.setNegativeButton("Cancel") { _, _ ->
            // User canceled the confirmation, do nothing
        }

        val dialog = builder.create()
        dialog.show()
    }


}
