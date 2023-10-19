package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class WastageMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_main)

        val wastageImage = findViewById<ImageView>(R.id.peliyagodaM)

        wastageImage.setOnClickListener {
            // Create an Intent to navigate to WastageFormC or another activity
            val intent = Intent(this, WastageOverview::class.java)

            // Start the new activity
            startActivity(intent)
        }
    }
}