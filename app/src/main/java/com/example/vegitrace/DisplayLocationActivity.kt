package com.example.vegitrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.gms.maps.model.LatLngBounds

class DisplayLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var database: FirebaseDatabase
    private lateinit var locationRef: DatabaseReference
    private var farmerName: String = "vimal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_location)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        locationRef = database.getReference("locations") // Reference to your location data in Firebase

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set the farmer's name (modify this to get the desired farmer's name)
        farmerName = "vimal" // Replace with the farmer's name you want to display
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (farmerName != null) {
            // Build a reference to the locations in the database for the specific farmer
            val farmerLocationRef = locationRef.child(farmerName!!)

            // Retrieve location data from Firebase for the specific farmer
            locationRef.addValueEventListener(object : ValueEventListener {
                // Inside your `onDataChange` function
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if there is data available
                    if (dataSnapshot.exists()) {
                        val builder = LatLngBounds.Builder()

                        for (locationSnapshot in dataSnapshot.children) {
                            val farmer = locationSnapshot.key // Get the farmer's name
                            if (farmer == farmerName) {
                                val latitude = locationSnapshot.child("latitude").value as Double
                                val longitude = locationSnapshot.child("longitude").value as Double
                                val location = LatLng(latitude, longitude)
                                builder.include(location)
                                val markerTitle = "Location Marker"

                                // Add a marker for the specified farmer's location
                                mMap.addMarker(MarkerOptions().position(location).title(markerTitle))
                            }
                        }

                        // Move the camera to show all markers
                        val bounds = builder.build()
                        val padding = 100 // Adjust padding as needed
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        mMap.animateCamera(cameraUpdate)
                    } else {
                        // Handle the case when there's no data (e.g., show a message)
                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors, if any
                    Log.e("DatabaseError", "Error: $databaseError")
                }
            })
        }
    }
}
