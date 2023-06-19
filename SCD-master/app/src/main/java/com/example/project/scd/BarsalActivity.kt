package com.example.project.scd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class BarsalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barsal)
        //navBackBar
        val navBack3 = findViewById<MaterialToolbar>(R.id.topAppBar3)
        navBack3.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }
}