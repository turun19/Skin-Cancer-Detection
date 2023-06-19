package com.example.project.scd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val btn_mulai = findViewById<Button>(R.id.btn_mulai)
        btn_mulai.setOnClickListener {
            val intent = Intent(this@StartActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        this.supportActionBar?.hide()
        this.actionBar?.hide()
    }
}