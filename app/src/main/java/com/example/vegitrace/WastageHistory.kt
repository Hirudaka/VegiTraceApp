package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class WastageHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_history)

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


}