package com.example.vegitrace

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.vegitrace.databinding.ActivityFarmerLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FarmerLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityFarmerLocationBinding
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFarmerLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onStart() {
        super.onStart()
        getCurrentLocationUser()
    }

    private fun getCurrentLocationUser() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val getLocation = fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(applicationContext, "${currentLocation?.latitude}, ${currentLocation?.longitude}", Toast.LENGTH_LONG).show()

                // Fetch the farmer's name using the current user's ID
                val userId = firebaseAuth.currentUser?.uid
                if (userId != null) {
                    val farmerNameRef = database.getReference("farmer").child(userId).child("name")
                    farmerNameRef.get().addOnSuccessListener { dataSnapshot ->
                        val farmerName = dataSnapshot.value as String
                        // Update the user's location in the database using the farmer's name as a unique identifier
                        updateLocationInDatabase(farmerName)

                        // Add a marker for the current location
                        addMarkerForCurrentLocation()
                    }
                }
            }
        }

    }

    private fun updateLocationInDatabase(farmerName: String) {
        val locationRef = database.getReference("locations").child(farmerName)

        // Get the latest location or provide a new location
        val latestLocation = currentLocation ?: createDefaultLocation()

        val latitude = latestLocation.latitude
        val longitude = latestLocation.longitude
        val timestamp = System.currentTimeMillis()

        val locationData = mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to timestamp
        )

        locationRef.setValue(locationData)
    }

    private fun addMarkerForCurrentLocation() {
        if (currentLocation != null) {
            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("Current Location")

            // You can customize the marker icon (e.g., make it red)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            mMap?.addMarker(markerOptions)
        } else {
            Toast.makeText(applicationContext, "Location not available", Toast.LENGTH_LONG).show()
        }
    }

    // A function to create a default location if a location is not available
    private fun createDefaultLocation(): Location {
        val defaultLocation = Location("")
        defaultLocation.latitude = 0.0 // Set a default latitude
        defaultLocation.longitude = 0.0 // Set a default longitude
        return defaultLocation
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationUser()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}
