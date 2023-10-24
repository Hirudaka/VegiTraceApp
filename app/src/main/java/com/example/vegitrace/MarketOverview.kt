package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

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

    }

    fun onCenterClicked(view: View) {
        val Center = view.tag.toString()



        val intent = Intent(this, VegeSelect::class.java)
        intent.putExtra("center", Center)

        Log.d("VegetableClick", "Selected Vegetable: $Center")

        startActivity(intent)
    }
}
