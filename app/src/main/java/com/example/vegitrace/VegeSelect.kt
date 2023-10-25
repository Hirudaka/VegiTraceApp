package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class VegeSelect : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var centerName: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vege_select)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val center = intent.getStringExtra("center")

        centerName = findViewById(R.id.center)

        if (center != null) {
            centerName.text = center
        } else {
            centerName.text = "Center Not Found" // Set a default value if the center is null
        }

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

    fun onVegetableClicked(view: View) {
        val vegetableName = view.tag.toString()
        val center = intent.getStringExtra("center")


        val intent = Intent(this, AddReserves::class.java)
        intent.putExtra("vegetableName", vegetableName)
        intent.putExtra("centerName", center)
        startActivity(intent)
    }
}
