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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WastageOverview : AppCompatActivity() {
    private lateinit var wastageShops2TextView: TextView
    private lateinit var wastageTotal2TextView: TextView
    private lateinit var wastageDate2TextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_overview)

        wastageShops2TextView = findViewById(R.id.wastageShops2)
        wastageTotal2TextView = findViewById(R.id.wastageTotal2)
        wastageDate2TextView = findViewById(R.id.wastageDate2)

        val dailyWastageRef = FirebaseDatabase.getInstance().reference.child("DailyWastage")
        val wastageRef = FirebaseDatabase.getInstance().reference.child("Wastage")

        // Calculate total wasteweight for DailyWastage
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

        // Get and display the current date
        val currentDate = getCurrentDate()
        wastageDate2TextView.text = "$currentDate"

        val wastageConfirmBtn = findViewById<Button>(R.id.wastageBookNow)
        wastageConfirmBtn.setOnClickListener {
            val intent = Intent(this, WastageForm::class.java)
            startActivity(intent)
        }

        val goBackButton = findViewById<Button>(R.id.wastageGoBack)
        goBackButton.setOnClickListener {
            val intent = Intent(this, WastageMain::class.java)
            startActivity(intent)
        }

        val wastageMainButton = findViewById<ImageView>(R.id.navHomeUnClick)
        val historyButton = findViewById<ImageView>(R.id.navBookingUnClick)
        val recyclerProfileButton = findViewById<ImageView>(R.id.navProfileUnClick)

        wastageMainButton.setOnClickListener {
            val intent = Intent(this, WastageMain::class.java)
            startActivity(intent)
        }

        historyButton.setOnClickListener {
            val intent = Intent(this, RecyclerRemoveList::class.java)
            startActivity(intent)
        }

        recyclerProfileButton.setOnClickListener {
            val intent = Intent(this, RecyclerProfile::class.java)
            startActivity(intent)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
