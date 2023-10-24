package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Review
import com.example.vegitrace.view.ReviewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopReview : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = ArrayList<Review>()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var searchBar: EditText
    private lateinit var myReviewsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_review)
        myReviewsButton = findViewById(R.id.button4)
        recyclerView = findViewById(R.id.reviewsRec)
        searchBar = findViewById(R.id.searchBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val reviewsRef: DatabaseReference = database.getReference("reviews")

        // Set up the RecyclerView adapter
        reviewAdapter = ReviewAdapter(this, reviewList)
        recyclerView.adapter = reviewAdapter

        val currentUser = auth.currentUser

        myReviewsButton.setOnClickListener {
            val intent = Intent(this, ReviewRemoveList::class.java)
            startActivity(intent)
        }

        if (currentUser != null) {
            val userId = currentUser.uid

            reviewsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    reviewList.clear()
                    for (reviewSnapshot in dataSnapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        if (review != null) {
                            reviewList.add(review)
                        }
                    }
                    reviewAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors that occur during the database read operation
                }
            })

            // Add a TextChangedListener to the search bar
            searchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Filter the reviews based on the entered text
                    filterReviews(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                    // Do nothing
                }
            })
        }
    }

    // Function to filter the reviews based on the entered text
    private fun filterReviews(query: String) {
        val filteredReviews = reviewList.filter { review ->
            review.farmername.contains(query, true)
        }

        // Update the RecyclerView with the filtered reviews
        reviewAdapter.updateReviews(filteredReviews)
    }
}
