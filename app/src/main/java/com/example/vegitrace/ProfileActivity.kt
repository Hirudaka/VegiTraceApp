package com.example.vegitrace

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val vehicleRegNo = intent.getStringExtra("vehicleRegNo")
        val qrCodeBase64 = intent.getStringExtra("qrCodeBase64")

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val addressTextView = findViewById<TextView>(R.id.addressTextView)
        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
        val vehicleRegNoTextView = findViewById<TextView>(R.id.vehicleRegNoTextView)
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)

        nameTextView.text = "Name: $name"
        emailTextView.text = "Email: $email"
        addressTextView.text = "Address: $address"
        phoneNumberTextView.text = "Phone Number: $phoneNumber"
        vehicleRegNoTextView.text = "Vehicle Registration Number: $vehicleRegNo"

        val decodedQRCode = decodeBase64ToBitmap(qrCodeBase64)
        qrCodeImageView.setImageBitmap(decodedQRCode)
    }

    private fun decodeBase64ToBitmap(base64: String?): Bitmap? {
        if (base64.isNullOrEmpty()) return null
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}