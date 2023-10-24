package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.databinding.ActivityShopOwnerProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.vegitrace.model.ShopOwner

class ShopOwnerProfileEdit : AppCompatActivity() {
    private lateinit var binding: ActivityShopOwnerProfileEditBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopOwnerProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        userId = currentUser.uid


        val databaseRef = database.getReference("shopOwners").child(userId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val shopOwner = dataSnapshot.getValue(ShopOwner::class.java)
                    if (shopOwner != null) {
                        // Populate the input fields with existing user data
                        binding.profileName.setText(shopOwner.name)
                        binding.profileAddress.setText(shopOwner.address)
                        binding.profileShopNo.setText(shopOwner.shopNo)
                        binding.profileMarketPosition.setText(shopOwner.marketPosition)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ShopOwnerProfileEdit, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
            }
        })

        binding.sellerUpdatePro.setOnClickListener {
            val name = binding.profileName.text.toString()
            val address = binding.profileAddress.text.toString()
            val shopNo = binding.profileShopNo.text.toString()
            val marketPosition = binding.profileMarketPosition.text.toString()

            val databaseRef = database.getReference("shopOwners").child(userId)
            databaseRef.child("name").setValue(name)
            databaseRef.child("address").setValue(address)
            databaseRef.child("shopNo").setValue(shopNo)
            databaseRef.child("marketPosition").setValue(marketPosition)

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
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
