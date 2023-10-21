package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vegitrace.databinding.ActivitySellerAddWasteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SellerAddWaste : AppCompatActivity() {

    private lateinit var binding: ActivitySellerAddWasteBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerAddWasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("DailyWastage")

        binding.AddWastageBtn.setOnClickListener {
            val shopno = binding.addwastageShop1.text.toString()
            val shopowner = binding.addWastageowner1.text.toString()
            val wasteweightStr = binding.addWastageweight1.text.toString()

            if (shopno.isNotEmpty() && shopowner.isNotEmpty() && wasteweightStr.isNotEmpty()) {
                val wasteweight = wasteweightStr.toIntOrNull()

                if (wasteweight != null) {
                    val waste = AddWaste(shopno, shopowner, wasteweight)
                    database.child(shopno).setValue(waste).addOnSuccessListener {

                        binding.addwastageShop1.text.clear()
                        binding.addWastageowner1.text.clear()
                        binding.addWastageweight1.text.clear()

                        Toast.makeText(this, "Form Successfully Added", Toast.LENGTH_LONG).show()

                        calculateTotalWasteWeightAndRedirect()
                        /*val intent = Intent(this, WastageOverview::class.java)
                        startActivity(intent)*/
                    }.addOnFailureListener {
                        Toast.makeText(this, "Form failed to add", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid weight input.Please enter a valid number.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }


        }
    }private fun calculateTotalWasteWeightAndRedirect() {
        var totalWasteWeight = 0 // Initialize the total to 0

        // Fetch the data from Firebase and update totalWasteWeight
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val waste = dataSnapshot.getValue(AddWaste::class.java)
                    waste?.wasteweight?.let {
                        totalWasteWeight += it
                    }
                }
                // Create an Intent to navigate to WastageOverview activity
                val intent = Intent(this@SellerAddWaste, WastageOverview::class.java)
                intent.putExtra("totalWasteWeight", totalWasteWeight)
                startActivity(intent)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SellerAddWaste, "Failed to calculate total waste weight", Toast.LENGTH_LONG).show()
            }
        })
    }
    data class AddWaste(
        val shopno:String?=null,
        val shopowner:String?=null,
        val wasteweight:Int?=null
    )
}