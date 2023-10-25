package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.Order
import com.example.vegitrace.model.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

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

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        currentUser = auth.currentUser!!

        if (currentUser != null) {
            val userId = currentUser.uid
            val shopOwnersRef: DatabaseReference = database.getReference("shopOwners")

            shopOwnersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val shopOwnerData = dataSnapshot.getValue(ShopOwner::class.java)
                        val ownerName = shopOwnerData?.name
                        val center = shopOwnerData?.marketPosition

                        if (ownerName != null) {
                            centreEditText.setText(center)
                            supplierEditText.setText(ownerName)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                }
            })

            // Create an ArrayAdapter for the Spinner and set values from resources
            val vegetableTypes = resources.getStringArray(R.array.vegetable_types)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vegetableTypes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            vegetableTypeSpinner.adapter = adapter

            vegetableTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
                    // Get a reference to your Firebase database
                    val ordersReference = database.getReference("orders")

                    // Check if the order ID already exists in the database
                    ordersReference.orderByChild("orderId").equalTo(orderId).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Order ID already exists, display an error message
                                Toast.makeText(this@AddOrderActivity, "Order ID already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                // Use the user information to create a unique order
                                val order = Order(orderId, shopowner, vegetableType, quantity, price, centre, "Pending", "")

                                // Push the data to the "orders" node, creating a new child node with a unique ID
                                val newOrderReference = ordersReference.push()
                                newOrderReference.setValue(order)
                                    .addOnSuccessListener {
                                        // Order insertion successful, display a success message
                                        Toast.makeText(this@AddOrderActivity, "Order inserted successfully", Toast.LENGTH_SHORT).show()

                                        // Optionally, clear the input fields
                                        orderIdEditText.text.clear()
                                        supplierEditText.text.clear()
                                        quantityEditText.text.clear()
                                        priceEditText.text.clear()
                                        centreEditText.text.clear()
                                    }
                                    .addOnFailureListener { e ->
                                        // Order insertion failed, display an error message
                                        Toast.makeText(this@AddOrderActivity, "Order insertion failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle the error
                        }
                    })
                }
            }
        }

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
        topProfile.setOnClickListener{
            val intent = Intent(this, ShopOwnerProfile::class.java)
            startActivity(intent)
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
            "BeetRoot" -> {
                vegetableNameTextView.text = "BeetRoot"
                imageView2.setImageResource(R.drawable.beet)
            }
            "EggPlant" -> {
                vegetableNameTextView.text = "EggPlant"
                imageView2.setImageResource(R.drawable.eggplant)
            }
            "Corn" -> {
                vegetableNameTextView.text = "Corn"
                imageView2.setImageResource(R.drawable.corn)
            }
        }
    }
}
