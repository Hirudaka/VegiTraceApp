package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vegitrace.model.WasteForm
import com.example.vegitrace.view.RecyclerRemoveAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecyclerRemoveList : AppCompatActivity(), RecyclerRemoveAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerRemoveAdapter: RecyclerRemoveAdapter
    private val wastageList = ArrayList<WasteForm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_remove_list)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerremoveRecyclerVIew)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve the user's ID from Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserID = currentUser?.uid

        val databaseReference = FirebaseDatabase.getInstance().reference.child("Wastage")

        if (currentUserID != null) {
            recyclerRemoveAdapter = RecyclerRemoveAdapter(this, wastageList, this, currentUserID)
            recyclerView.adapter = recyclerRemoveAdapter
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                wastageList.clear()
                for (snapshot in dataSnapshot.children) {
                    val wastage = snapshot.getValue(WasteForm::class.java)
                    wastage?.let {
                        wastageList.add(it)
                    }
                }
                recyclerRemoveAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })

        val historyButton = findViewById<ImageView>(R.id.navBookingUnClick)
        val wastageMainButton = findViewById<ImageView>(R.id.navHomeUnClick)
        val recyclerProfileButton = findViewById<ImageView>(R.id.navProfileUnClick)

        wastageMainButton.setOnClickListener {
            val intent = Intent(this, WastageMain::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener {
            val intent = Intent(this, RecyclerRemoveList::class.java)
            startActivity(intent)
        }
        recyclerProfileButton.setOnClickListener {
            val intent = Intent(this, RecyclerProfile::class.java)
            startActivity(intent)
        }


    }

    override fun onItemClick(position: Int) {
        val removedWastage = wastageList[position]
        removeWastageFromDatabase(removedWastage, position)
    }

    private fun removeWastageFromDatabase(wastage: WasteForm, position: Int) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Wastage")

        // Create a query to find the matching wastage by the "key" field
        val query = databaseReference.orderByChild("key").equalTo(wastage.key)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue() // Remove the data from the database
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }
}
