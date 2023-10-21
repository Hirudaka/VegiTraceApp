package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import com.example.vegitrace.model.FarmerData
import com.example.vegitrace.model.Review
import com.example.vegitrace.model.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class AddReview : AppCompatActivity() {
    private lateinit var farmerNameEditText: EditText
    private lateinit var ownerNameEditText: EditText
    private lateinit var farmerMailEditText: EditText
    private lateinit var reviewEditText: EditText
    private lateinit var submitReviewButton: Button
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        farmerNameEditText = findViewById(R.id.farmername)
        ownerNameEditText = findViewById(R.id.ownername)
        farmerMailEditText = findViewById(R.id.farmermail)
        reviewEditText = findViewById(R.id.review)
        submitReviewButton = findViewById(R.id.buttonSubmitReview)

        // Retrieve the authenticated user's name and set it as the owner's name
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val shopOwnersRef: DatabaseReference = database.getReference("shopOwners")

            shopOwnersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val shopOwnerData = dataSnapshot.getValue(ShopOwner::class.java)
                        val ownerName = shopOwnerData?.name

                        if (ownerName != null) {
                            ownerNameEditText.setText(ownerName)
                        } else {
                            Toast.makeText(this@AddReview, "User name not found. Please log in.", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@AddReview, "Error: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this@AddReview, "User not authenticated. Please log in.", Toast.LENGTH_LONG).show()
        }

        // Retrieve the scanned data as a JSON string
        val scannedData = intent.getStringExtra("scannedData")

        try {
            // Parse the JSON data from the scanned data
            val gson = Gson()
            val farmerData = gson.fromJson(scannedData, FarmerData::class.java)

            // Set the farmer's name and email fields from the scanned data
            farmerNameEditText.setText(farmerData.name)
            farmerMailEditText.setText(farmerData.email)
        } catch (e: JsonSyntaxException) {
            // Handle parsing error
            Toast.makeText(this, "Failed to parse scanned data", Toast.LENGTH_SHORT).show()
        }

        submitReviewButton.setOnClickListener {
            val farmerName = farmerNameEditText.text.toString()
            val ownerName = ownerNameEditText.text.toString()
            val farmerMail = farmerMailEditText.text.toString()
            val reviewText = reviewEditText.text.toString()

            if (farmerName.isNotEmpty() && ownerName.isNotEmpty() && farmerMail.isNotEmpty() && reviewText.isNotEmpty()) {
                saveReviewToFirebase(farmerName, ownerName, farmerMail, reviewText)
            } else {
                Toast.makeText(this, "Please provide all details.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveReviewToFirebase(farmerName: String, ownerName: String, email: String, review: String) {
        val reference = database.getReference("reviews")

        val newReview = Review(farmerName, ownerName, email, review)

        val key = reference.push().key
        if (key != null) {
            reference.child(key).setValue(newReview)
                .addOnSuccessListener {
                    Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Failed to submit review: ${error.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Failed to generate a key for the review.", Toast.LENGTH_LONG).show()
        }
    }
}
