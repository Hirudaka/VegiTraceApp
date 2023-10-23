package com.example.vegitrace

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.databinding.ActivitySignupBinding
import com.example.vegitrace.model.Farmer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.Hashtable

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val phoneNumber = binding.phone.text.toString()
            val vehicleRegNo = binding.regno.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Create a JSON object with registration details
                    val registrationDetails = JSONObject()
                    registrationDetails.put("name", name)
                    registrationDetails.put("email", email)
                    registrationDetails.put("address", address)
                    registrationDetails.put("phoneNumber", phoneNumber)
                    registrationDetails.put("vehicleRegNo", vehicleRegNo)

                    // Convert the JSON object to a string
                    val registrationDetailsString = registrationDetails.toString()

                    // Generate QR code for all registration details
                    val qrCodeBitmap = generateQRCode(registrationDetailsString)

                    // Convert the QR code to a Base64 string
                    val qrCodeBase64 = encodeBitmapToBase64(qrCodeBitmap)

                    // Attempt to create a new user with email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { authResult ->
                            if (authResult.isSuccessful) {
                                // Registration was successful, now we proceed to save data to Realtime Database
                                val userId = authResult.result?.user?.uid
                                if (userId != null) {
                                    val farmer = Farmer(name, email, address, phoneNumber, vehicleRegNo, qrCodeBase64)
                                    saveFarmerToDatabase(userId, farmer)

                                    // Pass the user data to ProfileActivity
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Failed to create user.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // Registration failed, don't add the user to Firebase Authentication
                                val errorMessage = authResult.exception?.message
                                Toast.makeText(this, "Registration failed. $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Passwords do not match
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Fields are empty
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun saveFarmerToDatabase(userId: String, farmer: Farmer) {
        val farmerRef = database.getReference("farmer").child(userId)
        farmerRef.setValue(farmer)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val errorMessage = task.exception?.message
                    Toast.makeText(this, "Data not saved. $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun generateQRCode(registrationDetails: String): Bitmap {
        val hints: Hashtable<EncodeHintType, ErrorCorrectionLevel> = Hashtable()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(registrationDetails, BarcodeFormat.QR_CODE, 300, 300, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) -0x1000000 else -0x1)
                }
            }
            return bmp
        } catch (e: Exception) {
            e.printStackTrace()
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return an empty bitmap in case of an error
        }
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}




