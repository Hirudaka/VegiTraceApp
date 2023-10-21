package com.example.vegitrace.model

class Recycle{
var name: String? = null
var address: String? = null
var phone: String? = null
var nic: String? = null
var email: String? = null


constructor() {
    // Default no-argument constructor required for Firebase
}

constructor(name: String?, address: String?, phone: String?, nic: String?, email: String?) {
    this.name = name
    this.address = address
    this.phone = phone
    this.nic = nic
    this.email = email

}
}