package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.util.Log
import android.widget.ImageView
import com.example.vegitrace.view.OrderAdapter


class Centers : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.centers)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val shopOwnersRef: DatabaseReference = database.getReference("shopOwners")

            shopOwnersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val shopOwnerData = dataSnapshot.getValue(ShopOwner::class.java)

                        val marketPosition = shopOwnerData?.marketPosition

                        if (shopOwnerData != null) {
                            if (marketPosition != null) {
                                val centerTextView = findViewById<TextView>(R.id.center)
                                centerTextView.text = marketPosition
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error, if any
                }
            })

            val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
            val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
            val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
            val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)
            val topProfile = findViewById<ImageView>(R.id.imageView4)

            navHomeUnClick.setOnClickListener {
                val intent = Intent(this, Centers::class.java)
                startActivity(intent)
            }
            navAddUnClick.setOnClickListener {
                val intent = Intent(this, AddOrderActivity::class.java)
                startActivity(intent)
            }
            navReviewUnClick.setOnClickListener {
                val intent = Intent(this, ShopReview::class.java)
                startActivity(intent)
            }
            navScanUnClick.setOnClickListener {
                val intent = Intent(this, QRscanner::class.java)
                startActivity(intent)
            }
            topProfile.setOnClickListener{
                val intent = Intent(this, ShopOwnerProfile::class.java)
                startActivity(intent)
            }
        }
    }

    fun onVegetableClicked(view: View) {
        val vegetableName = view.tag.toString()
        val centerName = findViewById<TextView>(R.id.center).text.toString()
        Log.d("VegetableClick", "Selected Vegetable: $vegetableName")
        Log.d("Centre", "Selected centre: $centerName")

        val intent = Intent(this, OrdersActivity::class.java)
        intent.putExtra("vegetableName", vegetableName)
        intent.putExtra("centerName", centerName)
        startActivity(intent)
    }
}
