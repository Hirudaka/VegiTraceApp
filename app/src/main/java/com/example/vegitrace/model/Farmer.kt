package com.example.vegitrace.model

 class Farmer {
    var name: String? = null
    var email: String? = null
    var address: String? = null
    var phoneNumber: String? = null
    var vehicleRegNo: String? = null
    var qrCodeBase64: String? = null // Added field for the QR code
    constructor() {
        // Default no-argument constructor required for Firebase
    }

    constructor(name: String?, email: String?, address: String?, phoneNumber: String?, vehicleRegNo: String?, qrCodeBase64: String?) : this() {
        this.name = name
        this.email = email
        this.address = address
        this.phoneNumber = phoneNumber
        this.vehicleRegNo = vehicleRegNo
        this.qrCodeBase64 = qrCodeBase64
    }
}
