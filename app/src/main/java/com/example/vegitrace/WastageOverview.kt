package com.example.vegitrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class WastageOverview : AppCompatActivity() {
    private lateinit var wastageShops2TextView: TextView
    private lateinit var wastageTotal2TextView: TextView
    private lateinit var wastageDate2TextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wastage_overview)

        wastageShops2TextView = findViewById(R.id.wastageShops2) // Replace with the actual ID of your TextView
        wastageTotal2TextView = findViewById(R.id.wastageTotal2)
        wastageDate2TextView = findViewById(R.id.wastageDate2)

        var database = FirebaseDatabase.getInstance().reference.child("DailyWastage")

        // Add a listener to count the children and update the TextView
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = snapshot.childrenCount.toInt()
                wastageShops2TextView.text = "$childCount Shops"
            }
            override fun onCancelled(error: DatabaseError) {
                wastageShops2TextView.text = "Failed to retrieve shop count"
            }
        })

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalSum = 0

                for (dataSnapshot in snapshot.children) {
                    val waste = dataSnapshot.getValue(SellerAddWaste.AddWaste::class.java)
                    waste?.wasteweight?.let {
                        totalSum += it
                    }
                }
                wastageTotal2TextView.text = "$totalSum Kg(s)"

                // Get and display the current date
                val currentDate = getCurrentDate()
                wastageDate2TextView.text = "$currentDate"
            }

            override fun onCancelled(error: DatabaseError) {
                wastageTotal2TextView.text = "Failed to calculate total waste weight"
            }
        })


        val wastageConfirmBtn = findViewById<Button>(R.id.wastageBookNow)

        wastageConfirmBtn.setOnClickListener {
            val intent = Intent(this, WastageForm::class.java)
            startActivity(intent)
        }
        // Add a button to go back to the WastageMain page
        val goBackButton = findViewById<Button>(R.id.wastageGoBack)

        goBackButton.setOnClickListener {
            val intent = Intent(this, WastageMain::class.java)
            startActivity(intent)
        }

        // Retrieve the total waste weight from the intent
        val totalWasteWeight = intent.getIntExtra("totalWasteWeight", 0) // 0 is the default value if the extra is not found

        // Find the TextView with ID "wastageTotal2" and set the value
        val wastageTotal2TextView = findViewById<TextView>(R.id.wastageTotal2)
        wastageTotal2TextView.text = totalWasteWeight.toString()

    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}