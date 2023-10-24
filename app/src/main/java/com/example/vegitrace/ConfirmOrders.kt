package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.ConfirmOrderAdapter
import com.google.firebase.database.*

class ConfirmOrders : AppCompatActivity(), ConfirmOrderAdapter.OnItemClickListener {
    private lateinit var confirmOrderAdapter: ConfirmOrderAdapter
    private val orderList = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.porderRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        // Set up the RecyclerView adapter
        confirmOrderAdapter = ConfirmOrderAdapter(this, orderList, this)
        recyclerView.adapter = confirmOrderAdapter

        // Read data from Firebase and populate the orderList
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                orderList.clear()
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(Order::class.java)
                    order?.let {
                        orderList.add(it)
                    }
                }
                confirmOrderAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }

    override fun onItemClick(position: Int) {
        val orderId = orderList[position].orderId
        removeOrderFromDatabase(orderId, position)
    }

    private fun removeOrderFromDatabase(orderId: String, position: Int) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")
        val orderQuery = databaseReference.orderByChild("orderId").equalTo(orderId)

        orderQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                    orderList.removeAt(position)
                    confirmOrderAdapter.itemRemovedAtUpdatedList(position)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }
}
