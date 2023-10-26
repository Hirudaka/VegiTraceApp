package com.example.vegitrace
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.vegitrace.databinding.ActivityShopOwnerProfileBinding
import com.example.vegitrace.model.ShopOwner

class ShopOwnerProfile : AppCompatActivity() {
    private lateinit var binding: ActivityShopOwnerProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopOwnerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser //check if user authenticated
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        userId = currentUser.uid
        val databaseRef = database.getReference("shopOwners").child(userId) // Query using the user's UID

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val shopOwner = dataSnapshot.getValue(ShopOwner::class.java)
                    if (shopOwner != null) {
                        // Populate the binding elements with user data
                        binding.profileName.setText(shopOwner.name)
                        binding.profileEmail.setText(shopOwner.email)
                        binding.profileAddress.setText(shopOwner.address)
                        binding.profileShopNo.setText(shopOwner.shopNo)
                        binding.profileMarketPosition.setText(shopOwner.marketPosition)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ShopOwnerProfile, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
            }
        })
        val sellerEditProButton = binding.sellerEditPro

        val logoutbtn = binding.shoplogout

        sellerEditProButton.setOnClickListener {
            val intent = Intent(this, ShopOwnerProfileEdit::class.java)
            startActivity(intent)
        }

        logoutbtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to log out?")

            builder.setPositiveButton("Yes") { _, _ ->
                val intent = Intent(this, Welcome::class.java)
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton("No") { _, _ ->
            }

            builder.show()
        }

        val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
        val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
        val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
        val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)
        val topProfile = findViewById<ImageView>(R.id.imageView4)
        val addDwastage = findViewById<Button>(R.id.sellerAddWaste)
        val sellerOrder = findViewById<Button>(R.id.sellerMyOrders)

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
        addDwastage.setOnClickListener {
            val intent = Intent(this, SellerAddWaste::class.java)
            startActivity(intent)
        }
        sellerOrder.setOnClickListener {
            val intent = Intent(this, MyOrdersActivity::class.java)
            startActivity(intent)
        }

    }
}

