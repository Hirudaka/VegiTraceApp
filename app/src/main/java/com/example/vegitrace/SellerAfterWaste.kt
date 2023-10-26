package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.vegitrace.model.WasteForm
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class SellerAfterWaste : AppCompatActivity() {
    private lateinit var wastageShops2TextView: TextView
    private lateinit var wastageTotal2TextView: TextView
    private lateinit var wastageDate2TextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_after_waste)

        wastageShops2TextView = findViewById(R.id.wastageShops2)
        wastageTotal2TextView = findViewById(R.id.wastageTotal2)
        wastageDate2TextView = findViewById(R.id.wastageDate2)

        var dailyWastageRef = FirebaseDatabase.getInstance().reference.child("DailyWastage")
        val wastageRef = FirebaseDatabase.getInstance().reference.child("Wastage")

        dailyWastageRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = snapshot.childrenCount.toInt()
                wastageShops2TextView.text = "$childCount Shops"
            }

            override fun onCancelled(error: DatabaseError) {
                wastageShops2TextView.text = "Failed to retrieve shop count"
            }
        })

        // Calculate total wasteweight for Wastage
        dailyWastageRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dailyWastageSnapshot: DataSnapshot) {
                var totalWasteWeightDailyWastage = 0

                for (dataSnapshot in dailyWastageSnapshot.children) {
                    val waste = dataSnapshot.getValue(SellerAddWaste.AddWaste::class.java)
                    waste?.wasteweight?.let {
                        totalWasteWeightDailyWastage += it
                    }
                }

                // Calculate total wasteweight for Wastage
                wastageRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(wastageSnapshot: DataSnapshot) {
                        var totalWasteWeightWastage = 0

                        for (dataSnapshot in wastageSnapshot.children) {
                            val waste = dataSnapshot.getValue(WasteForm::class.java)
                            waste?.wastageWeight?.let {
                                totalWasteWeightWastage += it
                            }
                        }
                        // Calculate the difference between DailyWastage and Wastage
                        val difference = totalWasteWeightDailyWastage - totalWasteWeightWastage

                        // Update the TextView with the difference
                        wastageTotal2TextView.text = "$difference Kg(s)"
                    }

                    override fun onCancelled(error: DatabaseError) {
                        wastageTotal2TextView.text = "Failed to calculate total waste weight for Wastage"
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                wastageTotal2TextView.text = "Failed to calculate total waste weight for DailyWastage"
            }
        })

        val currentDate = getCurrentDate()
        wastageDate2TextView.text = "$currentDate"


        val goBackButton = findViewById<Button>(R.id.wastageGoBack)
        goBackButton.setOnClickListener {
            val intent = Intent(this, ShopOwnerProfile::class.java)
            startActivity(intent)
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
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}