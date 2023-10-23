package com.example.vegitrace

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RecyclerProfile : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var nicTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_profile)

        // Initialize the TextView elements
        nameTextView = findViewById(R.id.nameTextView)
        addressTextView = findViewById(R.id.addressTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        nicTextView = findViewById(R.id.nicTextView)
        emailTextView = findViewById(R.id.emailTextView)

        // Initialize Firebase Database reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("Recyclers").child(userId ?: "")

        // Read data from the database and update TextViews
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val recycler = dataSnapshot.getValue(Recycler::class.java)

                    if (recycler != null) {
                        // Update the TextViews with retrieved data
                        nameTextView.text = "Name: ${recycler.name}"
                        addressTextView.text = "Address: ${recycler.address}"
                        phoneTextView.text = "Phone: ${recycler.phone}"
                        nicTextView.text = "NIC: ${recycler.nic}"
                        emailTextView.text = "Email: ${recycler.email}"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors during data retrieval
            }
        })
    }
}
