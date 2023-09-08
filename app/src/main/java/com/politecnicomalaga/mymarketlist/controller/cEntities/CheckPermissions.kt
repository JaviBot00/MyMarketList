package com.politecnicomalaga.mymarketlist.controller.cEntities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat

class CheckPermissions(private val fromActivity: Activity) {

    companion object {
        const val CAMERA_PERMISSION_REQUEST = 1001
        const val GALLERY_PERMISSION_REQUEST = 1002
        const val CAMERA_REQUEST = 2001
        const val GALLERY_REQUEST = 2002
    }

    fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            fromActivity, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission() {
        fromActivity.requestPermissions(
            arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
        )
    }

    fun startCameraActivity() {
        fromActivity.startActivityForResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST
        )
    }

    fun areStoragePermissionsGranted(): Boolean {
        val readPermissionGranted = ContextCompat.checkSelfPermission(
            fromActivity, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermissionGranted = ContextCompat.checkSelfPermission(
            fromActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return readPermissionGranted && writePermissionGranted
    }

    fun requestStoragePermissions() {
        fromActivity.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), GALLERY_PERMISSION_REQUEST
        )
    }

    fun startGalleryActivity() {
        fromActivity.startActivityForResult(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            GALLERY_REQUEST
        )
    }
}
