package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.model.ShopOwner
import com.example.vegitrace.view.ConfirmOrderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConfirmOrders : AppCompatActivity(), ConfirmOrderAdapter.OnItemClickListener {
    private lateinit var confirmOrderAdapter: ConfirmOrderAdapter
    private val orderList = ArrayList<Order>()
    private lateinit var ownerName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_orders)

        val recyclerView = findViewById<RecyclerView>(R.id.porderRecycler)
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

                        confirmOrderAdapter = ConfirmOrderAdapter(
                            this@ConfirmOrders,
                            orderList,
                            this@ConfirmOrders
                        )
                        recyclerView.adapter = confirmOrderAdapter

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
                                confirmOrderAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle any errors here
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors here
                }
            })

            val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
            val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
            val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
            val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)
            val topProfile = findViewById<ImageView>(R.id.imageView4)

            navHomeUnClick.setOnClickListener {
                val intent = Intent(this, Centers::class.java)
                startActivity(intent)
            }
            navAddUnClick.setOnClickListener {
                val intent = Intent(this, AddOrderActivity::class.java)
                startActivity(intent)
            }
            navReviewUnClick.setOnClickListener {
                val intent = Intent(this, ShopReview::class.java)
                startActivity(intent)
            }
            navScanUnClick.setOnClickListener {
                val intent = Intent(this, QRscanner::class.java)
                startActivity(intent)
            }
            topProfile.setOnClickListener {
                val intent = Intent(this, ShopOwnerProfile::class.java)
                startActivity(intent)
            }
        }
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
