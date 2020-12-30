package com.sylvie.boardgameguide.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker

object GetPhoto {

    private const val myPermissionsRequestRead = 0
    
    fun checkPermissionAndGetLocalImg(
        context: Context,
        activity: Activity,
        fragment: Fragment
    ) {
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
                myPermissionsRequestRead
            )
            getLocalImg(fragment)
        } else {
            getLocalImg(fragment)
        }
    }
    private fun getLocalImg(fragment: Fragment) {
        ImagePicker.with(fragment)
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


