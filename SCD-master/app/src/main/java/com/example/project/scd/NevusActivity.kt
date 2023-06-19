package com.example.project.scd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class NevusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nevus)
        //navBackBar
          val navBack5 = findViewById<MaterialToolbar>(R.id.topAppBar5)
        navBack5.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}