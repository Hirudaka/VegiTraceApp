package com.example.vegitrace
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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

        // Check if the user is authenticated
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // The user is not authenticated, handle this case (e.g., redirect to the login page).
            // You might also want to finish this activity.
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish() // Close this activity
            return
        }

        userId = currentUser.uid // Get the user's UID

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

        sellerEditProButton.setOnClickListener {
            val intent = Intent(this, ShopOwnerProfileEdit::class.java)
            startActivity(intent)
        }

        val navHomeUnClick = findViewById<ImageView>(R.id.navHomeUnClick)
        val navAddUnClick = findViewById<ImageView>(R.id.navAddUnClick)
        val navReviewUnClick = findViewById<ImageView>(R.id.navReviewUnClick)
        val navScanUnClick = findViewById<ImageView>(R.id.navScanUnClick)
        val topProfile = findViewById<ImageView>(R.id.imageView4)
        val addDWaste = findViewById<Button>(R.id.sellerAddWaste)

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
        addDWaste.setOnClickListener {
            val intent = Intent(this, SellerAddWaste::class.java)
            startActivity(intent)
        }
    }
}

