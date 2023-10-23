package com.example.vegitrace
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.workDataOf

class LocationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun doWork(): Result {
        if (checkPermissions()) {
            getLastLocation()
        } else {
            requestLocationPermissions()
        }
        return Result.success()
    }

    private fun checkPermissions(): Boolean {
        val context = applicationContext
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission)
    }

    private fun requestLocationPermissions() {
        val data = workDataOf("request_permissions" to true)
        val retryRequest = OneTimeWorkRequestBuilder<LocationWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(retryRequest)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    saveLocationToFirebase(location)
                }
        }
    }

    private fun saveLocationToFirebase(location: android.location.Location?) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val locationRef: DatabaseReference = database.getReference("locations")

        location?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            val timestamp = System.currentTimeMillis()
            val locationData = mapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "timestamp" to timestamp
            )
            locationRef.push().setValue(locationData)
        }
    }
}

object LocationWorkerUtil {
    fun createLocationUpdateRequest(): WorkRequest {
        return OneTimeWorkRequestBuilder<LocationWorker>()
            .setInitialDelay(0, TimeUnit.SECONDS) // Optional initial delay
            .build()
    }
}
