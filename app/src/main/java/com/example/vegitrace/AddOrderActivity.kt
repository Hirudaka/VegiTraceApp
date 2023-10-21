package com.example.vegitrace

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.Order

class AddOrderActivity : AppCompatActivity() {
    private lateinit var orderIdEditText: EditText
    private lateinit var supplierEditText: EditText
    private lateinit var vegetableTypeSpinner: Spinner
    private lateinit var quantityEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addVegetableButton: Button
    private lateinit var vegetableNameTextView: TextView
    private lateinit var imageView2: ImageView
    private lateinit var centreEditText: EditText

    private lateinit var currentUser: FirebaseUser
    private lateinit var auth: FirebaseAuth // Add this variable to store the Firebase Authentication instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
        orderIdEditText = findViewById(R.id.OrderIdeditText)
        supplierEditText = findViewById(R.id.SuppliereditText)
        vegetableTypeSpinner = findViewById(R.id.VegiNamespinner)
        quantityEditText = findViewById(R.id.QunatityeditText)
        priceEditText = findViewById(R.id.PriceeditText)
        addVegetableButton = findViewById(R.id.addVegetableButton)
        vegetableNameTextView = findViewById(R.id.VegetableNameTextView)
        imageView2 = findViewById(R.id.imageView2)
        centreEditText = findViewById(R.id.CentreeditTextText)

        // Initialize Firebase Authentication
        //auth = FirebaseAuth.getInstance()

        // Get the current user
        //currentUser = auth.currentUser!!

        // Check if a user is signed in
       // if (currentUser == null) {
            // Handle the case when no user is signed in, such as showing a login screen
           // Toast.makeText(this, "Please sign in to create an order", Toast.LENGTH_SHORT).show()
            // Optionally, navigate to a login screen or display a login dialog

        //}

        // Create an ArrayAdapter for the Spinner and set values from resources
        val vegetableTypes = resources.getStringArray(R.array.vegetable_types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vegetableTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vegetableTypeSpinner.adapter = adapter

        vegetableTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected vegetable type
                val selectedVegetableType = vegetableTypeSpinner.selectedItem.toString()

                // Update the UI components based on the selected vegetable type
                updateUIForVegetableType(selectedVegetableType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }

        addVegetableButton.setOnClickListener {
            // Retrieve data from UI elements
            val orderId = orderIdEditText.text.toString()
            val shopowner = supplierEditText.text.toString()
            val vegetableType = vegetableTypeSpinner.selectedItem.toString()
            val quantity = quantityEditText.text.toString()
            val price = priceEditText.text.toString()
            val centre = centreEditText.text.toString()

            // Check if any of the fields is empty
            if (orderId.isEmpty() || shopowner.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
                // Display a toast message indicating that all fields must be filled
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Get the user's unique ID (UID)
                val userId = currentUser.uid

                // Use the user information to create a unique order
                val order = Order(orderId, shopowner, vegetableType, quantity, price, centre, "Pending", userId)

                // Get a reference to your Firebase database
                val database = FirebaseDatabase.getInstance()
                val ordersReference = database.getReference("orders")

                // Push the data to the "orders" node, creating a new child node with a unique ID
                val newOrderReference = ordersReference.push()
                newOrderReference.setValue(order)
                    .addOnSuccessListener {
                        // Order insertion successful, display a success message
                        Toast.makeText(this, "Order inserted successfully", Toast.LENGTH_SHORT).show()

                        // Optionally, clear the input fields
                        orderIdEditText.text.clear()
                        supplierEditText.text.clear()
                        quantityEditText.text.clear()
                        priceEditText.text.clear()
                        centreEditText.text.clear()
                    }
                    .addOnFailureListener { e ->
                        // Order insertion failed, display an error message
                        Toast.makeText(this, "Order insertion failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun updateUIForVegetableType(vegetableType: String) {
        // Update the TextView and ImageView based on the selected vegetable type
        when (vegetableType) {
            "Carrot" -> {
                vegetableNameTextView.text = "Carrot"
                imageView2.setImageResource(R.drawable.carrots)
            }

            "Beans" -> {
                vegetableNameTextView.text = "Beans"
                imageView2.setImageResource(R.drawable.greenbeans)
            }

            "Cabbage" -> {
                vegetableNameTextView.text = "Cabbage"
                imageView2.setImageResource(R.drawable.cabbage)
            }
        }
    }
}
