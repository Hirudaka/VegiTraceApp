package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.MyReserveAdaptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyReserves : AppCompatActivity(),  MyReserveAdaptor.OnButtonClickListener {
    private lateinit var MyreserverAdapter: MyReserveAdaptor
    private val revList = ArrayList<Order>()
    private lateinit var user : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reserves)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        user = currentFirebaseUser!!.email.toString()



        // Initialize Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        // Set up the RecyclerView adapter


        MyreserverAdapter = MyReserveAdaptor(this, revList, this)
        recyclerView.adapter = MyreserverAdapter

        // Read data from Firebase and populate the orderList
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                revList.clear()
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(Order::class.java)
                    if (order?.farmer == user && order?.status == "Preparing"){
                    order?.let {
                        revList.add(it)
                    }}
                }
                MyreserverAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
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

    override fun onButtonClicked(position: Int) {
        // Handle the button click here
        // Update the 'status' attribute for the clicked order

        val id = revList[position].orderId




        val dbref = FirebaseDatabase.getInstance().getReference("orders")

        dbref.orderByChild("orderId").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (orderSnapshot in dataSnapshot.children) {
                        // Update the 'status' attribute for the matching order
                        val orderRef = dbref.child(orderSnapshot.key.toString())
                        orderRef.child("status").setValue("Pending")

                        // Update the 'farmer' attribute if needed
                        orderRef.child("farmer").setValue("")
                    }
                } else {
                    // Handle the case where the order with the given 'orderId' doesn't exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }
}
