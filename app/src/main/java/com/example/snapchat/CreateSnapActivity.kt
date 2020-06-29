package com.example.snapchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*


class CreateSnapActivity : AppCompatActivity() {

    var chooseSnapButton:Button? = null
    var snapImageView:ImageView? = null
    var captionEditText:EditText? = null
    var imageName = UUID.randomUUID().toString() + ".jpg"  // creates random name of the image file uploaded by the user
    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun chooseSnap(view:View){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            getPhoto()
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                val imageView =
                    findViewById<View>(R.id.snapImageView) as ImageView
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)
        snapImageView = findViewById(R.id.snapImageView)
        captionEditText = findViewById(R.id.captionEditText)
        chooseSnapButton = findViewById(R.id.chooseSnapButton)
    }

    fun saveAndShare(){
        // Get the data from an ImageView as bytes

        // Get the data from an ImageView as bytes
        snapImageView?.setDrawingCacheEnabled(true)
        snapImageView?.buildDrawingCache()
        val bitmap = (snapImageView?.getDrawable() as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this,"Sorry! Snap could not be uploaded",Toast.LENGTH_SHORT).show()
        }).addOnSuccessListener(OnSuccessListener<Any?> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
             Toast.makeText(this,"Snap uploaded successfully!", Toast.LENGTH_SHORT).show()
             var intent = Intent(this, ChooseUserActivity::class.java)
             intent.putExtra("imageName", imageName)
             intent.putExtra("caption", captionEditText?.text.toString() )
             startActivity(intent)
        })

    }
}


