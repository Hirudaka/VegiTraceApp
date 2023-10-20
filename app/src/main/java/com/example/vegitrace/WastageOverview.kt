package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class WastageOverview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_overview)

        val wastageConfirmBtn = findViewById<Button>(R.id.wastageBookNow)

        wastageConfirmBtn.setOnClickListener {
            val intent = Intent(this, WastageForm::class.java)
            startActivity(intent)
        }
        // Retrieve the total waste weight from the intent
        val totalWasteWeight = intent.getIntExtra("totalWasteWeight", 0) // 0 is the default value if the extra is not found

        // Find the TextView with ID "wastageTotal2" and set the value
        val wastageTotal2TextView = findViewById<TextView>(R.id.wastageTotal2)
        wastageTotal2TextView.text = totalWasteWeight.toString()
    }
}