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
        }
    }

    fun onVegetableClicked(view: View) {
        val vegetableName = view.tag.toString()
        val centerName = findViewById<TextView>(R.id.center).text.toString()
        Log.d("VegetableClick", "Selected Vegetable: $vegetableName")

        val intent = Intent(this, OrdersActivity::class.java)
        intent.putExtra("vegetableName", vegetableName)
        intent.putExtra("centerName", centerName)
        startActivity(intent)
    }
}
