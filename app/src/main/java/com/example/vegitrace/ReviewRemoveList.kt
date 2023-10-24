package com.example.vegitrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Review
import com.example.vegitrace.view.ReviewRemoveAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewRemoveList : AppCompatActivity(), ReviewRemoveAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewRemoveAdapter: ReviewRemoveAdapter
    private val reviewList = ArrayList<Review>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_remove_list)

        recyclerView = findViewById<RecyclerView>(R.id.reviewRemoveRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()

        // Get the currently logged-in user's name
        val currentUser = auth.currentUser
        val currentemail = currentUser?.email

        // Check if the current user is logged in and has a name
        if (currentemail != null) {
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

                        // Check if the review's farmername matches the current user's name
                        if (review?.owneremail == currentemail) {
                            review?.let {
                                reviewList.add(it)
                            }
                        }
                    }
                    reviewRemoveAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors here
                }
            })
        }
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
