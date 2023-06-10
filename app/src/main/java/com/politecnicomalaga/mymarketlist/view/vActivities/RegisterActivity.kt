package com.politecnicomalaga.mymarketlist.view.vActivities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cEntities.CheckPermissions
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.model.UserFeatures
import java.io.ByteArrayOutputStream
import java.util.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        private var myRegister: RegisterActivity? = null

        fun getInstance(): RegisterActivity {
            if (myRegister == null) {
                myRegister = RegisterActivity()
            }
            return myRegister!!
        }
    }

    private var imgProfile: ImageView? = null
    private var myImageBitMap: Bitmap? = null
    private var myImageByteArray: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        MainController().setAppBar(
            this@RegisterActivity, resources.getString(R.string.create_account)
        )
        MainController().backPressed(this@RegisterActivity)

        val textInputUsername: TextInputLayout = findViewById(R.id.txtFldUserName)
        val textInputPassword: TextInputLayout = findViewById(R.id.txtFldPassWord)
        val textInputEmail: TextInputLayout = findViewById(R.id.txtFldEmail)
        val btnSaveUser: Button = findViewById(R.id.btnSaveUser)

        val btnAddImg: FloatingActionButton = findViewById(R.id.btnAddImg)
        val btnSearchImg: FloatingActionButton = findViewById(R.id.btnSearchImg)
        imgProfile = findViewById(R.id.imgProfile)

        textInputUsername.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputUsername.error = null
            }
        }

        textInputPassword.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputPassword.error = null
            }
        }

        textInputEmail.editText?.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                textInputEmail.error = null
            }
        }

        btnAddImg.setOnClickListener {
            if (CheckPermissions(this@RegisterActivity).isCameraPermissionGranted()) {
                CheckPermissions(this@RegisterActivity).startCameraActivity()
            } else {
                CheckPermissions(this@RegisterActivity).requestCameraPermission()
            }
        }

        btnSearchImg.setOnClickListener {
            if (CheckPermissions(this@RegisterActivity).areStoragePermissionsGranted()) {
                CheckPermissions(this@RegisterActivity).startGalleryActivity()
            } else {
                CheckPermissions(this@RegisterActivity).requestStoragePermissions()
            }
        }

        btnSaveUser.setOnClickListener {
            val regex = Regex("^[a-zA-Z0-9]+$")
            val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
            val validUser = regex.matches(textInputUsername.editText!!.text)
            val validPassword = regex.matches(textInputPassword.editText!!.text)
            val validEmail = emailRegex.matches(textInputEmail.editText!!.text)
            if (textInputUsername.editText!!.text.isNullOrEmpty() || !validUser) {
                textInputUsername.error = "Enter a valid username"
                return@setOnClickListener
            }
            if (textInputPassword.editText!!.text.isNullOrEmpty() || !validPassword) {
                textInputPassword.error = "Enter a valid password"
                return@setOnClickListener
            }
            if (textInputEmail.editText!!.text.isNullOrEmpty() || !validEmail) {
                textInputEmail.error = "Enter a valid email"
                return@setOnClickListener
            }
            if (myImageByteArray == null) {
                MainController().showToast(this@RegisterActivity, R.string.select_image)
                return@setOnClickListener
            }
            val user = UserFeatures(
                textInputUsername.editText!!.text.toString().lowercase(Locale.ROOT),
                textInputPassword.editText!!.text.toString().lowercase(Locale.ROOT),
                textInputEmail.editText!!.text.toString().lowercase(Locale.ROOT),
                myImageByteArray!!,
                "/photos/" + textInputUsername.editText!!.text.toString()
                    .lowercase(Locale.ROOT) + ".png"
            )
            if (MainController().isConnected(this@RegisterActivity)) {
                ServerData(this@RegisterActivity).setServerUser(user)
            }
        }
    }

    fun endRegister(fromActivity: Activity) {
        val result = Intent(fromActivity, LoginActivity::class.java)
        fromActivity.setResult(RESULT_OK, result)
        fromActivity.finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                CheckPermissions.CAMERA_PERMISSION_REQUEST -> CheckPermissions(this@RegisterActivity).startCameraActivity()
                CheckPermissions.GALLERY_PERMISSION_REQUEST -> CheckPermissions(this@RegisterActivity).startGalleryActivity()
            }
        } else if (!CheckPermissions(this@RegisterActivity).isCameraPermissionGranted() && Manifest.permission.CAMERA in permissions) {
            showPermissionRequiredDialog()
        } else if (!CheckPermissions(this@RegisterActivity).areStoragePermissionsGranted() && (Manifest.permission.READ_EXTERNAL_STORAGE in permissions || Manifest.permission.WRITE_EXTERNAL_STORAGE in permissions)) {
            showPermissionRequiredDialog()
        } else {
            MainController().showToast(this@RegisterActivity, R.string.deny_permissions)
        }
    }

    private fun showPermissionRequiredDialog() {
        MaterialAlertDialogBuilder(this@RegisterActivity).setTitle(R.string.permissions_required_title)
            .setMessage(R.string.permissions_required_message)
            .setPositiveButton(R.string.go_to_settings) { _, _ ->
                openAppSettings()
            }.setNegativeButton(R.string.cancel, null).setCancelable(false).show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CheckPermissions.CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    myImageBitMap = data.extras!!.get("data") as Bitmap
                    val stream = ByteArrayOutputStream()
                    myImageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    myImageByteArray = stream.toByteArray()
                    imgProfile?.setImageBitmap(myImageBitMap)
                } else {
                    MainController().showToast(this@RegisterActivity, R.string.error_get_image)
                }
            }

            CheckPermissions.GALLERY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    myImageBitMap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(data.data as Uri))
                    val stream = ByteArrayOutputStream()
                    myImageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    myImageByteArray = stream.toByteArray()
                    imgProfile?.setImageBitmap(myImageBitMap)
                } else {
                    MainController().showToast(this@RegisterActivity, R.string.error_get_image)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myImageBitMap?.recycle()
        myImageBitMap = null
        myImageByteArray = null
        imgProfile = null
    }

}
