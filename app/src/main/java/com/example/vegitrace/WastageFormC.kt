package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WastageFormC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_form_c)

        val bookingConfirmButton = findViewById<Button>(R.id.bookingconfirm1)

        bookingConfirmButton.setOnClickListener {
            // Create an Intent to navigate to WastageFormC2
            val intent = Intent(this, WastageFormC2::class.java)

            // Start the new activity
            startActivity(intent)
        }

    }
}