package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import com.example.vegitrace.model.FarmerData
import com.example.vegitrace.model.Review
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class AddReview : AppCompatActivity() {
    private lateinit var farmerNameEditText: EditText
    private lateinit var farmerMailEditText: EditText
    private lateinit var reviewEditText: EditText
    private lateinit var submitReviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        farmerNameEditText = findViewById(R.id.farmername)
        farmerMailEditText = findViewById(R.id.farmermail)
        reviewEditText = findViewById(R.id.review)
        submitReviewButton = findViewById(R.id.buttonSubmitReview)

        // Retrieve the scanned data as a JSON string
        val scannedData = intent.getStringExtra("scannedData")

        try {
            // Parse the JSON data from the scanned data
            val gson = Gson()
            val farmerData = gson.fromJson(scannedData, FarmerData::class.java)

            // Set the name and email fields from the scanned data
            farmerNameEditText.setText(farmerData.name)
            farmerMailEditText.setText(farmerData.email)
        } catch (e: JsonSyntaxException) {
            // Handle parsing error
            Toast.makeText(this, "Failed to parse scanned data", Toast.LENGTH_SHORT).show()
        }

        submitReviewButton.setOnClickListener {
            val farmerName = farmerNameEditText.text.toString()
            val farmerMail = farmerMailEditText.text.toString()
            val reviewText = reviewEditText.text.toString()

            if (farmerName.isNotEmpty() && farmerMail.isNotEmpty() && reviewText.isNotEmpty()) {
                saveReviewToFirebase(farmerName, farmerMail, reviewText)
            } else {
                Toast.makeText(this, "Please provide all details.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveReviewToFirebase(name: String, email: String, review: String) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("reviews")

        val newReview = Review(name, email, review)

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

