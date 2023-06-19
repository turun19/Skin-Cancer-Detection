package com.example.project.scd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class BenignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benign)
        //navBackBar
        val navBack6 = findViewById<MaterialToolbar>(R.id.topAppBar6)
        navBack6.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}