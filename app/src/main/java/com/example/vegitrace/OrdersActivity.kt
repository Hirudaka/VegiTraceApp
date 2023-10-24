package com.example.vegitrace

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.OrderAdapter
import com.google.firebase.database.*

class OrdersActivity : AppCompatActivity() {
    private lateinit var orderAdapter: OrderAdapter
    private val orderList = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.oRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val vegetableName = intent.getStringExtra("vegetableName")

        val centerName = intent.getStringExtra("centerName")

        Log.d("VegetableClick", "Selected Vegetable: $vegetableName")



        // Initialize Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        // Set up the RecyclerView adapter
        orderAdapter = OrderAdapter(this, orderList)
        recyclerView.adapter = orderAdapter

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
                    orderAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors here
                }
            })
    }
}