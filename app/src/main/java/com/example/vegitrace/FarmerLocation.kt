package com.example.vegitrace

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.vegitrace.databinding.ActivityFarmerLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FarmerLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityFarmerLocationBinding
    private var currentLocation: Location? = null // Change to nullable type
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    // Initialize Firebase
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val locationRef: DatabaseReference = database.getReference("locations")

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

                // Store the location data in Firebase
                currentLocation?.let {
                    // Send latitude, longitude, and timestamp as separate fields
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val timestamp = System.currentTimeMillis()

                    // Create a map to store the data
                    val locationData = mapOf(
                        "latitude" to latitude,
                        "longitude" to longitude,
                        "timestamp" to timestamp
                    )

                    // Push the new location data to the database
                    locationRef.push().setValue(locationData)
                }

                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
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
        if (currentLocation != null) {
            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("Current Location")

            googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            googleMap?.addMarker(markerOptions)
        } else {
            Toast.makeText(applicationContext, "Location not available", Toast.LENGTH_LONG).show()
        }
    }
}
