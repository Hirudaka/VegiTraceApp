package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

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
        val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
        val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
        val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
        val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)


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

    }

    fun onCenterClicked(view: View) {
        val Center = view.tag.toString()


        val intent = Intent(this, OrdersActivity::class.java)
        intent.putExtra("center", Center)

        startActivity(intent)
    }
}
