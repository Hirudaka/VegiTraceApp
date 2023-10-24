package com.example.vegitrace
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.databinding.ActivityShopRegBinding
import com.example.vegitrace.model.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class shopReg : AppCompatActivity() {
    private lateinit var binding: ActivityShopRegBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Populate the market Spinner with options
        val markets = resources.getStringArray(R.array.market_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, markets)
        binding.market.adapter = adapter

        binding.signupButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val shopNo = binding.shopno.text.toString()
            val marketPosition = binding.market.selectedItem.toString() // Corrected

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Attempt to create a user in Firebase Authentication
                    createShopownerInFirebaseAuth(email, password) { userId ->
                        if (userId != null) {
                            // User creation was successful, now save to the database
                            val shopOwner = ShopOwner(name, email, address, shopNo,marketPosition )
                            saveShopownerToDatabase(userId, shopOwner)

                            val intent = Intent(this, shopLogin::class.java)
                            intent.putExtra("name", name)
                            intent.putExtra("email", email)
                            intent.putExtra("address", address)
                            intent.putExtra("shopNo", shopNo)
                            intent.putExtra("marketPosition", marketPosition)
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
            val loginIntent = Intent(this, shopLogin::class.java)
            startActivity(loginIntent)
        }
    }

    private fun createShopownerInFirebaseAuth(email: String, password: String, callback: (String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    callback(authResult.result?.user?.uid)
                } else {
                    val errorMessage = authResult.exception?.message
                    Log.e("FirebaseAuth", "User creation failed: $errorMessage")
                    callback(null)
                }
            }
    }

    private fun saveShopownerToDatabase(userId: String, shopOwner: ShopOwner) {
        val shopownerRef = database.getReference("shopOwners").child(userId)
        shopownerRef.setValue(shopOwner)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val errorMessage = task.exception?.message
                    Log.e("FirebaseDatabase", "Data not saved: $errorMessage")
                    Toast.makeText(this, "Data not saved. $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
}



