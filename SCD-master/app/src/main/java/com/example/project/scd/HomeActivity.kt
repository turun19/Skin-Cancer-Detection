package com.example.project.scd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.project.scd.R.drawable.barsal
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        this.supportActionBar?.show()

        //intent artikel
        val cvBarsal = findViewById<ImageButton>(R.id.basal)
        cvBarsal.setOnClickListener{
            startActivity(Intent(this,BarsalActivity::class.java))
        }
        val cvMelanoma = findViewById<ImageButton>(R.id.melanoma)
        cvMelanoma.setOnClickListener {
            startActivity(Intent(this,MelanomaActivity::class.java))
        }
        val cvNevus = findViewById<ImageButton>(R.id.nevus)
        cvNevus.setOnClickListener {
            startActivity(Intent(this,NevusActivity::class.java))
        }
        val cvBenign = findViewById<ImageButton>(R.id.keratosis)
        cvBenign.setOnClickListener {
            startActivity(Intent(this, BenignActivity::class.java))
        }

        //navBackBar
        val navBack2 = findViewById<MaterialToolbar>(R.id.topAppBar2)
        navBack2.setOnClickListener {
            startActivity(Intent(this,StartActivity::class.java))
        }

        val btnDetect = findViewById<FloatingActionButton>(R.id.btn_detect)
        btnDetect.setOnClickListener {
            val intent = Intent(this@HomeActivity,DetectActivity::class.java)
            startActivity(intent)
        }

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.barsal,"Basal cell carcinoma"))
        imageList.add(SlideModel(R.drawable.melanoma,"Melanoma"))
        imageList.add(SlideModel(R.drawable.nevus,"Nevus"))
        imageList.add(SlideModel(R.drawable.pigment_benign,"Pigmented benign keratosis"))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)



    }

}