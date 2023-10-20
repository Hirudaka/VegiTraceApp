package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vegitrace.databinding.ActivityWastageFormBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
                        // Pass the wastageWeight back to the WastageOverview activity
                        val intent = Intent()
                        intent.putExtra("wastageWeight", wastageWeight)
                        setResult(RESULT_OK, intent)
                        finish()

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
    }
    data class WasteForm(
        val wastageName: String?=null,
        val wastagePhone: String?=null,
        val wastageWeight: Int?=null,
        val wastageDate: String?=null)
}
