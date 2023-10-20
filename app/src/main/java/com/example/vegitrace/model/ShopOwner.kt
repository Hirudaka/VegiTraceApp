package com.example.vegitrace.model

class ShopOwner {
    var name: String? = null
    var email: String? = null
    var address: String? = null
    var shopNo: String? = null
    var marketPosition: String? = null

    constructor() {
        // Default no-argument constructor required for Firebase
    }

    constructor(name: String?, email: String?, address: String?, shopNo: String?, marketPosition: String?) {
        this.name = name
        this.email = email
        this.address = address
        this.shopNo = shopNo
        this.marketPosition = marketPosition
    }
}