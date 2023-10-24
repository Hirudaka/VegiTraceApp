package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.view.ReviewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.example.vegitrace.model.Review

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

        val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
        val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
        val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
        val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)
        val topProfile = findViewById<ImageView>(R.id.imageView4)

        navHomeUnClick.setOnClickListener {
            val intent = Intent(this, Centers::class.java)
            startActivity(intent)
        }
        navAddUnClick.setOnClickListener {
            val intent = Intent(this, AddOrderActivity::class.java)
            startActivity(intent)
        }
        navReviewUnClick.setOnClickListener {
            val intent = Intent(this, ShopReview::class.java)
            startActivity(intent)
        }
        navScanUnClick.setOnClickListener {
            val intent = Intent(this, QRscanner::class.java)
            startActivity(intent)
        }
        topProfile.setOnClickListener{
            val intent = Intent(this, ShopOwnerProfile::class.java)
            startActivity(intent)
        }
    }
}
