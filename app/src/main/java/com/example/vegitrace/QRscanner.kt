package com.example.vegitrace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.google.gson.Gson

class QRscanner : AppCompatActivity() {
    private lateinit var scannedDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        scannedDataTextView = findViewById(R.id.scannedDataTextView)

        // Create an instance of IntentIntegrator and initiate the scan
        val integrator = IntentIntegrator(this)
        integrator.setOrientationLocked(false)  // Allow both portrait and landscape scanning
        integrator.initiateScan()
    }

    // Handle the result in onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // The scanned data is in result.contents
                val scannedData = result.contents

                // Pass the scanned data to the AddReview activity
                val intent = Intent(this, AddReview::class.java)
                intent.putExtra("scannedData", scannedData)
                startActivity(intent)
            } else {
                // Handle case where the user canceled the scan
                scannedDataTextView.text = "Scanning canceled"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}


