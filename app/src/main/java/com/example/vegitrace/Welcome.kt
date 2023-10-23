package com.example.vegitrace


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Find the buttons
        val shopOwnerButton = findViewById<Button>(R.id.button)
        val recyclerButton = findViewById<Button>(R.id.button2)
        val farmerButton = findViewById<Button>(R.id.button3)

        // Set click listeners for the buttons
        shopOwnerButton.setOnClickListener { // Redirect to the Shop Owner login activity
            val intent = Intent(this@Welcome, shopLogin::class.java)
            startActivity(intent)
        }
        recyclerButton.setOnClickListener { // Redirect to the Recycler login activity
            val intent = Intent(this@Welcome, RecyclerLogin::class.java)
            startActivity(intent)
        }
        farmerButton.setOnClickListener { // Redirect to the Farmer login activity
            val intent = Intent(this@Welcome, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}