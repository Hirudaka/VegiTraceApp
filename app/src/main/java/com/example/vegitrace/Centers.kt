package com.example.vegitrace

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Centers : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.centers) // Replace with the name of your layout XML file

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()



        // Retrieve the authenticated user's data
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


                            // Set the marketPosition in the center text area
                            if (marketPosition != null) {
                                val centerTextView = findViewById<TextView>(R.id.center) // Replace with the ID of your center TextView
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
}
