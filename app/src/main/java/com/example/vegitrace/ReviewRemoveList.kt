package com.example.vegitrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Review
import com.example.vegitrace.view.ReviewRemoveAdapter
import com.google.firebase.database.*

class ReviewRemoveList : AppCompatActivity(), ReviewRemoveAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewRemoveAdapter: ReviewRemoveAdapter
    private val reviewList = ArrayList<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_remove_list)

        recyclerView = findViewById<RecyclerView>(R.id.reviewRemoveRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("reviews")

        // Set up the RecyclerView adapter
        reviewRemoveAdapter = ReviewRemoveAdapter(this, reviewList, this)
        recyclerView.adapter = reviewRemoveAdapter

        // Read data from Firebase and populate the reviewList
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reviewList.clear()
                for (snapshot in dataSnapshot.children) {
                    val review = snapshot.getValue(Review::class.java)
                    review?.let {
                        reviewList.add(it)
                    }
                }
                reviewRemoveAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }

    override fun onItemClick(position: Int) {
        val removedReview = reviewList[position]
        removeReviewFromDatabase(removedReview, position)
    }

    private fun removeReviewFromDatabase(review: Review, position: Int) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("reviews")

        // Create a query to find the matching review by both farmername and review fields
        val query = databaseReference.orderByChild("farmername").equalTo(review.farmername)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val dbReview = snapshot.getValue(Review::class.java)
                    if (dbReview != null && dbReview.review == review.review) {
                        snapshot.ref.removeValue()
                        reviewList.removeAt(position)
                        reviewRemoveAdapter.itemRemovedAtUpdatedList(position)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }
}
