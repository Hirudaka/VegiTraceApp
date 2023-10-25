package com.example.vegitrace

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.Farmer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Retrieve the user's profile information from Firebase Database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val databaseRef = FirebaseDatabase.getInstance().getReference("farmer").child(userId.toString())
        // Find the reservations button
        val reservationsButton = findViewById<Button>(R.id.reservation)

// Set an OnClickListener for the reservations button
        reservationsButton.setOnClickListener {
            // Define an Intent to start the ReservationsActivity
            val reservationsIntent = Intent(this, MyOrdersActivity::class.java)

            // Start the ReservationsActivity
            startActivity(reservationsIntent)
        }


        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val farmer = dataSnapshot.getValue(Farmer::class.java)

                    // Display the user's profile information
                    if (farmer != null) {
                        val nameTextView = findViewById<TextView>(R.id.nameTextView)
                        val emailTextView = findViewById<TextView>(R.id.emailTextView)
                        val addressTextView = findViewById<TextView>(R.id.addressTextView)
                        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
                        val vehicleRegNoTextView = findViewById<TextView>(R.id.vehicleRegNoTextView)
                        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)

                        nameTextView.text = farmer.name
                        emailTextView.text = farmer.email
                        addressTextView.text = farmer.address
                        phoneNumberTextView.text = farmer.phoneNumber
                        vehicleRegNoTextView.text = farmer.vehicleRegNo

                        // Decode and display the QR code
                        val decodedQRCode = decodeBase64ToBitmap(farmer.qrCodeBase64)
                        qrCodeImageView.setImageBitmap(decodedQRCode)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors or provide feedback to the user
                Toast.makeText(this@ProfileActivity, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
            }
        })

        val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
        val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
        val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
        val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)
        val farmerMap = findViewById<Button>(R.id.farmerLocation)
        val farRes = findViewById<Button>(R.id.reservation)

        navHomeUnClick.setOnClickListener {
            val intent = Intent(this, MarketOverview::class.java)
            startActivity(intent)
        }
        navAddUnClick.setOnClickListener {
            val intent = Intent(this, MyReserves::class.java)
            startActivity(intent)
        }
        navReviewUnClick.setOnClickListener {
            val intent = Intent(this, Reviews::class.java)
            startActivity(intent)
        }
        navScanUnClick.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        farmerMap.setOnClickListener {
            val intent = Intent(this, FarmerLocation::class.java)
            startActivity(intent)
        }
        val farmerEditProButton = findViewById<Button>(R.id.farmerEditPro)

        farmerEditProButton.setOnClickListener {
            val intent = Intent(this, FarmerProfileEdit::class.java)
            startActivity(intent)
        }
        farRes.setOnClickListener {
            val intent = Intent(this, MyReserves::class.java)
            startActivity(intent)
        }
    }


    private fun decodeBase64ToBitmap(base64: String?): Bitmap? {
        if (base64.isNullOrEmpty()) return null
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
