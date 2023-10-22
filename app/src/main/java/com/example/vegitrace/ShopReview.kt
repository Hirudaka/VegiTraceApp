package com.example.vegitrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.view.ReviewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import Review

class ShopReview : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = ArrayList<Review>()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_review)

        recyclerView = findViewById(R.id.reviewsRec)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val reviewsRef: DatabaseReference = database.getReference("reviews")

        // Set up the RecyclerView adapter
        reviewAdapter = ReviewAdapter(this, reviewList)
        recyclerView.adapter = reviewAdapter

        val currentUser = auth.currentUser

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
        }
    }
}
