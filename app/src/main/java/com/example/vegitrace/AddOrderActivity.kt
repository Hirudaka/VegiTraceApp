package com.example.vegitrace
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.ImageView
import com.google.firebase.database.FirebaseDatabase
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.view.View


class AddOrderActivity : AppCompatActivity() {
    private lateinit var orderIdEditText: EditText
    private lateinit var supplierEditText: EditText
    private lateinit var vegetableTypeSpinner: Spinner
    private lateinit var quantityEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addVegetableButton: Button
    private lateinit var vegetableNameTextView: TextView
    private lateinit var imageView2: ImageView

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
            val supplier = supplierEditText.text.toString()
            val vegetableType = vegetableTypeSpinner.selectedItem.toString()
            val quantity = quantityEditText.text.toString()
            val price = priceEditText.text.toString()

            // Create an Order object with default values
            val order = Order(orderId, supplier, vegetableType, quantity, price, "Pending", null)

            // Get a reference to your Firebase database
            val database = FirebaseDatabase.getInstance()
            val ordersReference = database.getReference("orders")

            // Push the data to the "orders" node, creating a new child node with a unique ID
            val newOrderReference = ordersReference.push()
            newOrderReference.setValue(order)

            // Optionally, display a message to confirm the insertion
            // You can add a TextView to your layout to display a confirmation message
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


data class Order(
    val orderId: String,
    val supplier: String,
    val vegetableType: String,
    val quantity: String,
    val price: String,
    val status: String, // Default to "Pending"
    val farmer: String? // Default to null
)
