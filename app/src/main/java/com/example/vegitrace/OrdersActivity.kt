package com.example.vegitrace

import android.annotation.SuppressLint
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

        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        orderAdapter = OrderAdapter(this, orderList)
        recyclerView.adapter = orderAdapter

        databaseReference
            .orderByChild("centerName")
            .equalTo(centerName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    orderList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val order = snapshot.getValue(Order::class.java)
                        order?.let {
                            if (it.vegetableType == vegetableName) {
                                Log.d("VegetableClick", "Selected Vegetable: $vegetableName")
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
