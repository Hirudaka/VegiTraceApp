package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class VegeSelect : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var centerName: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vege_select)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val center = intent.getStringExtra("center")

        centerName = findViewById(R.id.center)

        if (center != null) {
            centerName.text = center
        } else {
            centerName.text = "Center Not Found" // Set a default value if the center is null
        }







    }

    fun onVegetableClicked(view: View) {
        val vegetableName = view.tag.toString()
        val center = intent.getStringExtra("center")


        val intent = Intent(this, AddReserves::class.java)
        intent.putExtra("vegetableName", vegetableName)
        intent.putExtra("centerName", center)
        startActivity(intent)
    }
}
