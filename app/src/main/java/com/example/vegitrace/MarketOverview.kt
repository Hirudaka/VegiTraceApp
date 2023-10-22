package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ImageView

class MarketOverview : AppCompatActivity() {
    private lateinit var dambullaImageView: ImageView
    private lateinit var meegodaImageView: ImageView
    private lateinit var paliyagodaImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_overview)

        // Locate the ImageView elements by their IDs
        dambullaImageView = findViewById(R.id.dambullaloc)
        meegodaImageView = findViewById(R.id.meegodaloc)
        paliyagodaImageView = findViewById(R.id.paliyagodalock)





        // Set up OnClickListener for Paliyagoda ImageView
        paliyagodaImageView.setOnClickListener {
            // Navigate to the PaliyagodaActivity
            val intent = Intent(this, Centers::class.java)
            startActivity(intent)
        }
    }
}
