package com.sylvie.boardgameguide.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.github.dhaval2404.imagepicker.ImagePicker

object GetPhoto {

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0

    fun checkPermission(context: Context, activity: Activity) {
        val permission = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //if not having permission, ask for it
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
            getLocalImg(activity)
        } else {
            getLocalImg(activity)
        }

    }

    private fun getLocalImg(activity: Activity) {
        ImagePicker.with(activity)
            //Crop image(Optional), Check Customization for more option
            .crop()
            //Final image size will be less than 1 MB(Optional)
            .compress(1024)
            //Final image resolution will be less than 1080 x 1080(Optional)
            .maxResultSize(
                1080,
                1080
            )
            .start()
    }

}


