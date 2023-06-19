package com.example.project.scd

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.log

class DetectActivity : AppCompatActivity() {
    //tensorFlow 23-31
    private lateinit var mClassifier: Classifier
    private lateinit var mBitmap: Bitmap

    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2

    private val mInputSize = 224
    private val mModelPath = "model.tflite"
    private val mLabelPath = "labels.txt"
    private val mSamplePath = "skin-icon.jpg"

//    private lateinit var builder : AlertDialog.Builder

    //api
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //tensorFlow
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_detect)
        mClassifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
        this.supportActionBar?.show()

        //navigation bar back
        val navBack = findViewById<MaterialToolbar>(R.id.topAppBar)
        navBack.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        resources.assets.open(mSamplePath).use{
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
//            val mPhotoImageView = findViewById<ImageView>(R.id.mPhotoImageView)
//            mPhotoImageView.setImageBitmap(mBitmap)
        }
//        builder = AlertDialog.Builder(this)


        val mCameraButton = findViewById<FloatingActionButton>(R.id.mCameraButton)
        mCameraButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCameraIntent, mCameraRequestCode)
        }

        val mGalleryButton = findViewById<FloatingActionButton>(R.id.mGalleryButton)
        mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
        }
        val mDetectButton = findViewById<FloatingActionButton>(R.id.mDetectButton)
        findViewById<ImageView>(R.id.mPhotoImageView).tag = R.drawable.skin_icon
        mDetectButton.setOnClickListener {
            val mPhotoImageView = findViewById<ImageView>(R.id.mPhotoImageView)
            //baris 104-110 adalah conditional ketika gambar tidak ada, mengeluarkan popUp Toast.makeText
            if (mPhotoImageView.getDrawable() == null){
//                builder.setTitle("Allert!").setMessage("Masukkan Gambar Terlebih Dahulu!").setCancelable(true)
//                    .setPositiveButton("Yes"){dialogInterfaces,it ->
//                        finish()
//                    }
//                    .show()
                Toast.makeText(this,"Masukkan gambar terlebih dahulu",Toast.LENGTH_SHORT).show()
            //baris 112-116 berfungsi menginput hasil kedalam textView
            }else {
                val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
                val mResultTextView = findViewById<TextView>(R.id.mResultTextView)
                mResultTextView.text =
                    "\n Jenis Kanker: " + results?.title
                val mResultTextView2 = findViewById<TextView>(R.id.mResultTextView2)
                mResultTextView2.text =
                  "\n Akurasi:" + results?.confidence
                mPhotoImageView.isEnabled = true

            }
        }
        //baris 122-128 berfungsi menghantar pengguna jika mengklik textView diarahkan ke google
        //dengan hasil yang diterima
        val mResultTextView = findViewById<TextView>(R.id.mResultTextView)
        mResultTextView.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https:://www.google.com/search?q=${mResultTextView.text}")
            )
            startActivity(intent)
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val mPhotoImageView = findViewById<ImageView>(R.id.mPhotoImageView)
        val mResultTextView = findViewById<TextView>(R.id.mResultTextView)
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == mCameraRequestCode){
            //buka camera dan galeri
            if(resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = data.extras!!.get("data") as Bitmap
                mBitmap = scaleImage(mBitmap)
                val toast = Toast.makeText(this, ("Image crop to: w= ${mBitmap.width} h= ${mBitmap.height}"), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 20)
                toast.show()
                mPhotoImageView.setImageBitmap(mBitmap)
                mResultTextView.text= "Your photo image set now."
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show()
            }
        } else if(requestCode == mGalleryRequestCode) {
            if (data != null) {
                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                println("Success!!!")
                mBitmap = scaleImage(mBitmap)
                mPhotoImageView.setImageBitmap(mBitmap)

            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()

        }


    }


    //baris 176-183 berfungsi untuk mengubah ukuran gambar yang di ambil menggunakan kamera. sesuai dengan model kita
    //yang mengharapkan bentuk input yang tepat(224 x 224 pixels)
    fun scaleImage(bitmap: Bitmap?): Bitmap {
        val orignalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / orignalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true)
    }


}