package com.example.vegitrace


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Reviews : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference
    private lateinit var user : String
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewArrayList: ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        reviewRecyclerView = findViewById(R.id.recyclerView)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewRecyclerView.setHasFixedSize(true)

        reviewArrayList = arrayListOf<Review>()
        getBlogData()


    }

    private fun getBlogData() {

        dbRef = FirebaseDatabase.getInstance().getReference("reviews")

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        user = currentFirebaseUser!!.email.toString()

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val rev = userSnapshot.getValue(Review::class.java)
                        if (rev?.email == user) {

                            reviewArrayList.add(rev!!)

                        }

                    }

                    reviewRecyclerView.adapter = ReviewAdaptor(reviewArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
        val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
        val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
        val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)

        navHomeUnClick.setOnClickListener {
            val intent = Intent(this, MarketOverview::class.java)
            startActivity(intent)
        }
        navAddUnClick.setOnClickListener {
            val intent = Intent(this, MyReserves::class.java)
            startActivity(intent)
        }
        navReviewUnClick.setOnClickListener {
            val intent = Intent(this, Reviews::class.java)
            startActivity(intent)
        }
        navScanUnClick.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

}