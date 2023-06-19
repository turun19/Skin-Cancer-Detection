package com.example.project.scd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class MelanomaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_melanoma)


        val navBack4 = findViewById<MaterialToolbar>(R.id.topAppBar4)
        navBack4.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }
}