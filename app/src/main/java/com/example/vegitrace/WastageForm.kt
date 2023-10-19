package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WastageForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_form)

        val wastageConfirmBtn = findViewById<Button>(R.id.WastageConfirmBtn)

        wastageConfirmBtn.setOnClickListener {
            // Create an Intent to navigate to WastageFormC
            val intent = Intent(this, WastageFormC::class.java)

            // Start the new activity
            startActivity(intent)
        }
    }
}