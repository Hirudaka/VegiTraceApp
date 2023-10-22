package com.example.vegitrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.MyOrderAdapter
import com.google.firebase.database.*

class MyOrdersActivity : AppCompatActivity(), MyOrderAdapter.OnItemClickListener {
    private lateinit var myOrderAdapter: MyOrderAdapter
    private val orderList = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.myRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        val vegetableName = intent.getStringExtra("vegetableName")
        val centerName = intent.getStringExtra("centerName")


        // Set up the RecyclerView adapter
        myOrderAdapter = MyOrderAdapter(this, orderList, this)
        recyclerView.adapter = myOrderAdapter

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
                myOrderAdapter.notifyDataSetChanged()
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
                    myOrderAdapter.itemRemovedAtUpdatedList(position)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }
}
