package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.vegitrace.databinding.ActivityRecyclersignupBinding
import com.example.vegitrace.model.Recycler
class RecyclerSignup : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclersignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclersignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signupButton1.setOnClickListener {
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val phone = binding.phone.text.toString()
            val nic = binding.nic.text.toString()
            val email = binding.signupEmail1.text.toString()
            val password = binding.signupPassword1.text.toString()
            val confirmPassword = binding.signupConfirm1.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Attempt to create a user in Firebase Authentication
                    createAccount(email, password) { success ->
                        if (success) {
                            // User creation was successful, now save to the database
                            saveUserDataToDatabase(name, address, phone, nic, email)

                            // Navigate to the next activity
                            val intent = Intent(this, RecyclerLogin::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Failed to create user.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginRedirectText.setOnClickListener {
            // Handle login redirection here
        }
    }

    private fun createAccount(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    callback(true)
                } else {
                    val errorMessage = authResult.exception?.message
                    Toast.makeText(this, "User creation failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            }
    }

    private fun saveUserDataToDatabase(name: String, address: String, phone: String, nic: String, email: String) {
        // Replace the following lines with your database logic to save user data
        val databaseRef = database.getReference("Recyclers")
        val recycler = Recycler(name, address, phone, nic, email)
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            databaseRef.child(userId).setValue(recycler)
        }
    }
}
