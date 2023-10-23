package com.example.vegitrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.AddReserveAdaptor
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*

class AddReserves : AppCompatActivity(),  AddReserveAdaptor.OnButtonClickListener {
    private lateinit var reserverAdapter: AddReserveAdaptor
    private val orderList = ArrayList<Order>()
    private lateinit var user : String


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
                        if (it.centre == centerName) {
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

    override fun onButtonClicked(order: Order) {
        // Handle the button click here
        // Update the 'status' attribute for the clicked order

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        user = currentFirebaseUser!!.email.toString()
        val orderToUpdate = orderList.find { it.orderId == order.orderId }
        if (orderToUpdate != null) {
            orderToUpdate.status = "Preparing"
            orderToUpdate.farmer = user


            reserverAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
        }
}
}
