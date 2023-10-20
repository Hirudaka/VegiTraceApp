package com.example.vegitrace.model

data class Farmer(
    val name: String,
    val email: String,
    val address: String,
    val phoneNumber: String,
    val vehicleRegNo: String,
    val qrCodeBase64: String // Add this field for the QR code
)