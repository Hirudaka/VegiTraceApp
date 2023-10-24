package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.vegitrace.databinding.ActivityWastageFormBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.vegitrace.model.WasteForm

class WastageForm : AppCompatActivity() {

    private lateinit var binding: ActivityWastageFormBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWastageFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Wastage")
        binding.WastageConfirmBtn.setOnClickListener {
            val wastageName = binding.WastageNameEdit.text.toString()
            val wastagePhone = binding.WastagePhoneEdit.text.toString()
            val wastageWeightStr = binding.WastageWeightEdit.text.toString()
            val wastageDate = binding.WastageDateEdit.text.toString()

            if (wastageName.isNotEmpty() && wastagePhone.isNotEmpty() && wastageWeightStr.isNotEmpty() && wastageDate.isNotEmpty()) {
                val wastageWeight = wastageWeightStr.toIntOrNull()

                if (wastageWeight != null) {
                    val wastage = WasteForm(wastageName, wastagePhone, wastageWeight, wastageDate)
                    database.child(wastageName).setValue(wastage).addOnSuccessListener {

                        binding.WastageNameEdit.text.clear()
                        binding.WastagePhoneEdit.text.clear()
                        binding.WastageWeightEdit.text.clear()
                        binding.WastageDateEdit.text.clear()

                        Toast.makeText(this, "Form Successfully Added", Toast.LENGTH_LONG).show()
                        // Navigate to the WasteFormC2 activity
                        val intent = Intent(this, WastageFormC2::class.java)
                        startActivity(intent)
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
        binding.wastageGoBack.setOnClickListener {
            finish()
        }


        val historyButton = findViewById<ImageView>(R.id.navBookingUnClick)
        val wastageMainButton = findViewById<ImageView>(R.id.navHomeUnClick)
        val recyclerProfileButton = findViewById<ImageView>(R.id.navProfileUnClick)

        wastageMainButton.setOnClickListener {
            val intent = Intent(this, WastageMain::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener {
            val intent = Intent(this, WastageHistory::class.java)
            startActivity(intent)
        }
        recyclerProfileButton.setOnClickListener {
            val intent = Intent(this, RecyclerProfile::class.java)
            startActivity(intent)
        }

    }
    data class WasteForm(
        val wastageName: String?=null,
        val wastagePhone: String?=null,
        val wastageWeight: Int?=null,
        val wastageDate: String?=null)
}
