package com.example.vegitrace

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.AddReserveAdaptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddReserves : AppCompatActivity(), AddReserveAdaptor.OnButtonClickListener {
    private lateinit var reserverAdapter: AddReserveAdaptor
    private val orderList = ArrayList<Order>()
    private lateinit var user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reserves)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val vegetableName = intent.getStringExtra("vegetableName")
        val centerName = intent.getStringExtra("centerName")

        // Initialize Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        // Set up the RecyclerView adapter
        reserverAdapter = AddReserveAdaptor(this, orderList, this)
        recyclerView.adapter = reserverAdapter

        // Read data from Firebase and populate the orderList
        databaseReference
            .orderByChild("vegetableType")
            .equalTo(vegetableName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    orderList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val order = snapshot.getValue(Order::class.java)
                        order?.let {
                            if (it.centre == centerName && it.status == "Pending") {
                                orderList.add(it)
                            }
                        }
                    }
                    reserverAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors here
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onButtonClicked(position: Int) {
        // Handle the button click here
        val id = orderList[position].orderId
        val status = "Preparing"

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        user = currentFirebaseUser?.email.toString()

        val dbref = FirebaseDatabase.getInstance().getReference("orders")

        dbref.orderByChild("orderId").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (orderSnapshot in dataSnapshot.children) {
                        // Update the 'status' attribute for the matching order
                        val orderRef = dbref.child(orderSnapshot.key.toString())
                        orderRef.child("status").setValue(status)

                        // Update the 'farmer' attribute if needed
                        orderRef.child("farmer").setValue(user)
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
