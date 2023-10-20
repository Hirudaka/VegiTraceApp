package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vegitrace.databinding.ActivityDbtestingBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DBtesting : AppCompatActivity() {

    private lateinit var binding: ActivityDbtestingBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbtestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttondb.setOnClickListener{
            val testname1 = binding.testname1.text.toString()
            val testpass1 = binding.testpass1.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User(testname1,testpass1)
            database.child(testname1).setValue(User).addOnSuccessListener {

                binding.testname1.text.clear()
                binding.testpass1.text.clear()

                Toast.makeText(this,"Successfully added",Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
            }

        }
    }
}