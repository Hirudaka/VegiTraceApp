package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase

class TestForm : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var insertButton: Button
    private val database = FirebaseDatabase.getInstance()
    private val dataReference = database.getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_form)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        insertButton = findViewById(R.id.insertButton)

        insertButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()

            val user = User(name, email) // Create a data class to hold the data

            // Push data to the database
            val dataKey = dataReference.push().key
            if (dataKey != null) {
                dataReference.child(dataKey).setValue(user)
            }
        }
    }
}

data class User(val name: String, val email: String)
