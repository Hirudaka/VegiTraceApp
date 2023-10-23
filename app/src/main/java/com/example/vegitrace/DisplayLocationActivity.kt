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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_location)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        locationRef = database.getReference("locations") // Reference to your location data in Firebase

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Retrieve location data from Firebase
        locationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (locationSnapshot in dataSnapshot.children) {
                    val latitude = locationSnapshot.child("latitude").value as Double
                    val longitude = locationSnapshot.child("longitude").value as Double
                    val location = LatLng(latitude, longitude)
                    val markerTitle = "Location Marker"

                    // Add a marker for each location
                    mMap.addMarker(MarkerOptions().position(location).title(markerTitle))
                }

                // Move the camera to show all markers
                val builder = LatLngBounds.Builder()
                for (locationSnapshot in dataSnapshot.children) {
                    val latitude = locationSnapshot.child("latitude").value as Double
                    val longitude = locationSnapshot.child("longitude").value as Double
                    builder.include(LatLng(latitude, longitude))
                }
                val bounds = builder.build()
                val padding = 100 // Adjust padding as needed
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.animateCamera(cameraUpdate)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors, if any
                Log.e("DatabaseError", "Error: $databaseError")
            }
        })
    }
}
