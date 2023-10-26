package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.vegitrace.databinding.ActivitySellerAddWasteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SellerAddWaste : AppCompatActivity() {

    private lateinit var binding: ActivitySellerAddWasteBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerAddWasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val shopOwnersRef = FirebaseDatabase.getInstance().reference.child("shopOwners").child(currentUserId)
            shopOwnersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val shopno = snapshot.child("shopNo").getValue(String::class.java)
                        val shopowner = snapshot.child("name").getValue(String::class.java)

                        binding.addwastageShop1.setText(shopno)
                        binding.addWastageowner1.setText(shopowner)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SellerAddWaste, "Failed to retrieve user information", Toast.LENGTH_LONG).show()
                }
            })
        }

        database = FirebaseDatabase.getInstance().reference.child("DailyWastage")

        binding.AddWastageBtn.setOnClickListener {
            val shopno = binding.addwastageShop1.text.toString()
            val shopowner = binding.addWastageowner1.text.toString()
            val wasteweightStr = binding.addWastageweight1.text.toString()

            if (shopno.isNotEmpty() && shopowner.isNotEmpty() && wasteweightStr.isNotEmpty()) {
                val wasteweight = wasteweightStr.toIntOrNull()

                if (wasteweight != null) {
                    val waste = AddWaste(shopno, shopowner, wasteweight)
                    database.child(shopno).setValue(waste).addOnSuccessListener {

                        binding.addwastageShop1.text.clear()
                        binding.addWastageowner1.text.clear()
                        binding.addWastageweight1.text.clear()

                        Toast.makeText(this, "Form Successfully Added", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, SellerAfterWaste::class.java)
                        startActivity(intent)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Form failed to add", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid weight input.Please enter a valid number.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }


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
    data class AddWaste(
        val shopno:String?=null,
        val shopowner:String?=null,
        val wasteweight:Int?=null
    )
}