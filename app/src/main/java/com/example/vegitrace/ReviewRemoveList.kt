package com.example.vegitrace


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Review
import com.example.vegitrace.view.ReviewRemoveAdapter
import com.google.firebase.database.*

class ReviewRemoveList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewRemoveAdapter: ReviewRemoveAdapter
    private val reviewList = ArrayList<Review>()
    private lateinit var database: FirebaseDatabase
    private lateinit var reviewsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_remove_list)

        recyclerView = findViewById(R.id.reviewRemoveRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reviewRemoveAdapter = ReviewRemoveAdapter(this, reviewList)
        recyclerView.adapter = reviewRemoveAdapter

        database = FirebaseDatabase.getInstance()
        reviewsRef = database.getReference("reviews")

        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reviewList.clear() // Clear the existing data

                for (reviewSnapshot in dataSnapshot.children) {
                    val reviewData = reviewSnapshot.getValue(Review::class.java)
                    reviewData?.let { reviewList.add(it) }
                }

                reviewRemoveAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors, such as no internet connection or database access issues
            }
        })
    }
}
