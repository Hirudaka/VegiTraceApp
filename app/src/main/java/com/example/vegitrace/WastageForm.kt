package com.example.vegitrace

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.vegitrace.databinding.ActivityWastageFormBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.vegitrace.model.WasteForm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WastageForm : AppCompatActivity() {

    private lateinit var binding: ActivityWastageFormBinding
    private lateinit var database: DatabaseReference
    private val calendar = Calendar.getInstance()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWastageFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Wastage")

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val recyclersRef = FirebaseDatabase.getInstance().reference.child("Recyclers").child(currentUserId)
            recyclersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.child("name").getValue(String::class.java)
                        val userPhone = snapshot.child("phone").getValue(String::class.java)

                        // Set the retrieved values in the EditText fields
                        binding.WastageNameEdit.setText(userName)
                        binding.WastagePhoneEdit.setText(userPhone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@WastageForm, "Failed to retrieve user information", Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.WastageDateEdit.setOnClickListener {
            showDatePicker()
        }

        binding.WastageConfirmBtn.setOnClickListener {
            val wastageName = binding.WastageNameEdit.text.toString()
            val wastagePhone = binding.WastagePhoneEdit.text.toString()
            val wastageWeightStr = binding.WastageWeightEdit.text.toString()
            val wastageDate = binding.WastageDateEdit.text.toString()

            if (wastageName.isNotEmpty() && wastagePhone.isNotEmpty() && wastageWeightStr.isNotEmpty() && wastageDate.isNotEmpty()) {
                val wastageWeight = wastageWeightStr.toIntOrNull()

                if (wastageWeight != null) {
                    val wastage = WasteForm(wastageName, wastagePhone, wastageWeight, wastageDate)
                    val newWastageRef = database.push()
                    newWastageRef.setValue(wastage).addOnSuccessListener {

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
    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Set the selected date in the EditText
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy /MM /dd", Locale.getDefault())
            binding.WastageDateEdit.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000
        datePickerDialog.show()
    }
    data class WasteForm(
        val wastageName: String?=null,
        val wastagePhone: String?=null,
        val wastageWeight: Int?=null,
        val wastageDate: String?=null)
}
