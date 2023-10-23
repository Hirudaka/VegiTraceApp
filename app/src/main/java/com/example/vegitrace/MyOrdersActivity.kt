package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.model.ShopOwner
import com.example.vegitrace.view.MyOrderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyOrdersActivity : AppCompatActivity(), MyOrderAdapter.OnItemClickListener, MyOrderAdapter.OnTrackButtonClickListener {
    private lateinit var myOrderAdapter: MyOrderAdapter
    private val orderList = ArrayList<Order>()
    private lateinit var ownerName: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.myRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val shopOwnersRef: DatabaseReference = database.getReference("shopOwners")

            shopOwnersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val shopOwnerData = dataSnapshot.getValue(ShopOwner::class.java)
                        ownerName = shopOwnerData?.name ?: ""

                        myOrderAdapter = MyOrderAdapter(
                            this@MyOrdersActivity,
                            orderList,
                            this@MyOrdersActivity,
                            this@MyOrdersActivity
                        )
                        recyclerView.adapter = myOrderAdapter

                        val databaseReference = database.reference.child("orders")
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                orderList.clear()
                                for (snapshot in dataSnapshot.children) {
                                    val order = snapshot.getValue(Order::class.java)
                                    order?.let {
                                        if (order.shopOwner == ownerName) {
                                            orderList.add(it)
                                        }
                                    }
                                }
                                myOrderAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle any errors here
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                }
            })
        }
    }

    override fun onTrackButtonClick(position: Int) {
        // Handle the "Track" button click here, e.g., start a new activity
        val intent = Intent(this, DisplayLocationActivity::class.java)
        intent.putExtra("orderId", orderList[position].orderId)
        startActivity(intent)
    }

    override fun onItemClick(position: Int) {
        val orderId = orderList[position].orderId
        removeOrderFromDatabase(orderId, position)
    }

    private fun removeOrderFromDatabase(orderId: String, position: Int) {
        val databaseReference = database.reference.child("orders")
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
