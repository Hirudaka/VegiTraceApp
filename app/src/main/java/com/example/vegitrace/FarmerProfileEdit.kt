package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.vegitrace.model.Farmer

class FarmerProfileEdit : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var vehicleRegNoEditText: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_profile_edit)

        nameEditText = findViewById(R.id.nameTextView)
        addressEditText = findViewById(R.id.addressTextView)
        phoneNumberEditText = findViewById(R.id.phoneNumberTextView)
        vehicleRegNoEditText = findViewById(R.id.vehicleRegNoTextView)
        updateButton = findViewById(R.id.farmerUpdatePro)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val databaseRef = FirebaseDatabase.getInstance().getReference("farmer").child(userId.toString())

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val farmer = dataSnapshot.getValue(Farmer::class.java)

                    if (farmer != null) {
                        nameEditText.setText(farmer.name)
                        addressEditText.setText(farmer.address)
                        phoneNumberEditText.setText(farmer.phoneNumber)
                        vehicleRegNoEditText.setText(farmer.vehicleRegNo)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val address = addressEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            val vehicleRegNo = vehicleRegNoEditText.text.toString()

            // Update the data in Firebase Database
            databaseRef.child("name").setValue(name)
            databaseRef.child("address").setValue(address)
            databaseRef.child("phoneNumber").setValue(phoneNumber)
            databaseRef.child("vehicleRegNo").setValue(vehicleRegNo)

            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
