package com.example.vegitrace


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }

}