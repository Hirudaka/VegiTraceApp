package com.example.vegitrace

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DisplayLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var database: FirebaseDatabase
    private lateinit var locationRef: DatabaseReference
    private lateinit var farmerName: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_location)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        locationRef = database.getReference("locations")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val farmerName = intent.getStringExtra("farmer")


    }

    override fun onMapReady(googleMap: GoogleMap) {

        val farmerName = intent.getStringExtra("farmer")
        mMap = googleMap

        if (farmerName != null) {
            // Build a reference to the locations in the database for the specific farmer
            val farmerLocationRef = locationRef.child(farmerName!!)


            locationRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

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
                        val padding = 100
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        mMap.animateCamera(cameraUpdate)

                    } else {

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
