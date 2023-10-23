package com.example.vegitrace

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecyclerProfileEdit : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var nicEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_profile_edit)

        // Initialize EditText elements
        nameEditText = findViewById(R.id.nameTextView)
        addressEditText = findViewById(R.id.addressTextView)
        phoneEditText = findViewById(R.id.phoneTextView)
        nicEditText = findViewById(R.id.nicTextView)
        saveButton = findViewById(R.id.recyclerUpdatePro)


        val userId = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("Recyclers").child(userId ?: "")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val recycler = dataSnapshot.getValue(Recycler::class.java)

                    if (recycler != null) {
                        nameEditText.setText(recycler.name)
                        addressEditText.setText(recycler.address)
                        phoneEditText.setText(recycler.phone)
                        nicEditText.setText(recycler.nic)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedAddress = addressEditText.text.toString()
            val updatedPhone = phoneEditText.text.toString()
            val updatedNic = nicEditText.text.toString()

            databaseReference.child("name").setValue(updatedName)
            databaseReference.child("address").setValue(updatedAddress)
            databaseReference.child("phone").setValue(updatedPhone)
            databaseReference.child("nic").setValue(updatedNic)

            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
