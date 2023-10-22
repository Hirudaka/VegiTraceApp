package com.example.vegitrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.Order
import com.example.vegitrace.view.MyReserveAdaptor
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*

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
    }

    override fun onButtonClicked(order: Order) {
        // Handle the button click here
        // Update the 'status' attribute for the clicked order

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        user = currentFirebaseUser!!.email.toString()
        val orderToUpdate = revList.find { it.orderId == order.orderId }
        if (orderToUpdate != null) {
            orderToUpdate.status = "Pending"
            orderToUpdate.farmer = null.toString()


            MyreserverAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
        }
    }
}
