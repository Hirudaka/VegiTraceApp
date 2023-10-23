package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vegitrace.model.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.util.Log
import com.example.vegitrace.view.OrderAdapter


class VegeSelect : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var centerName: TextView

    val center = intent.getStringExtra("center")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vege_select)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        centerName = findViewById(R.id.center)

        if (center != null) {
            centerName.text = center
        } else {
            centerName.text = "Center Not Found" // Set a default value if the center is null
        }







    }

    fun onVegetableClicked(view: View) {
        val vegetableName = view.tag.toString()


        val intent = Intent(this, AddReserves::class.java)
        intent.putExtra("vegetableName", vegetableName)
        intent.putExtra("centerName", center)
        startActivity(intent)
    }
}
