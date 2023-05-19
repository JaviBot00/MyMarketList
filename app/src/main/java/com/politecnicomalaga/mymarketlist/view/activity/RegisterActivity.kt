package com.politecnicomalaga.mymarketlist.view.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R

import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

     lateinit var textInputUser: TextInputLayout
     lateinit var textInputPassword: TextInputLayout
     lateinit var textInputBirthday: TextInputLayout
     lateinit var textInputUserGroup: TextInputLayout
     lateinit var imgProfile: ImageView
     lateinit var btnAddImg: FloatingActionButton
     lateinit var btnSearchImg: FloatingActionButton
     var myImageBitMap: Bitmap? = null
     var myImageByteArray: ByteArray? = null

    val CAMERA_PERMISSION_REQUEST = 1
    val CAMERA_REQUEST = 10
    val GALLERY_PERMISSION_REQUEST = 2
    val GALLERY_REQUEST = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textInputUser = findViewById(R.id.txtFldUser)
        textInputPassword = findViewById(R.id.txtFldPassWord)
        textInputBirthday = findViewById(R.id.txtFldEmail)
        imgProfile = findViewById(R.id.imgProfile)
        btnAddImg = findViewById(R.id.btnAddImg)
        btnSearchImg = findViewById(R.id.btnSearchImg)

        textInputUser.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    textInputUser.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        textInputPassword.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    textInputPassword.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build()
        textInputBirthday.editText?.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(supportFragmentManager, "tag")
            }
        }

        datePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            val date = Date(it)
            textInputBirthday.editText?.setText(simpleDateFormat.format(date))
        }

//        val mySQLite = MySQLiteManager(this@RegisterActivity)
//        mySQLite.setWritable()
//        val myCursor = mySQLite.getGroups()
//        val options = arrayOfNulls<String>(myCursor.count)
//        var cont = 0
//        while (myCursor.moveToNext()) {
//            options[cont] = myCursor.getString(0)
//            cont++
//        }
//        mySQLite.getDb().close()
//        textInputUserGroup.editText?.setOnClickListener {
//            MaterialAlertDialogBuilder(it.context).setTitle("Choose Rol")
//                .setSingleChoiceItems(options, -1, ({ dialogInterface: DialogInterface, i: Int ->
//                    textInputUserGroup.editText!!.setText(options[i])
//                })).setPositiveButton("Accept") { dialogInterface: DialogInterface?, i: Int ->
//                    textInputUserGroup.error = null
//                }.setNegativeButton("Cancel") { dialogInterface: DialogInterface?, i: Int ->
//                    textInputUserGroup.editText!!.text.clear()
//                }.show()
//        }

        btnAddImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@RegisterActivity, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
                )
            } else if (ContextCompat.checkSelfPermission(
                    this@RegisterActivity, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(
                    Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                    ), CAMERA_REQUEST
                )
            }
        }

        btnSearchImg.setOnClickListener {
            if ((ContextCompat.checkSelfPermission(
                    this@RegisterActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(
                    this@RegisterActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED)
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), GALLERY_PERMISSION_REQUEST
                )
            } else if (ContextCompat.checkSelfPermission(
                    this@RegisterActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this@RegisterActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(
                    Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ), GALLERY_REQUEST
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(
                        Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                        ), CAMERA_REQUEST
                    )
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
                    )
                }
            }

            GALLERY_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(
                        Intent(
                            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        ), GALLERY_REQUEST
                    )
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), GALLERY_PERMISSION_REQUEST
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                myImageBitMap = data.extras?.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                myImageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                myImageByteArray = stream.toByteArray()
                imgProfile.setImageBitmap(myImageBitMap)
            }
        }

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                myImageBitMap =
                    BitmapFactory.decodeStream(contentResolver.openInputStream(data.data as Uri))
                val stream = ByteArrayOutputStream()
                myImageBitMap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                myImageByteArray = stream.toByteArray()
                imgProfile.setImageBitmap(myImageBitMap)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Warning")
            .setMessage("Si sale de esta pantalla, perdera los datos escritos \n¿Esta seguro de salir?")
            .setPositiveButton("SI") { dialogInterface: DialogInterface?, i: Int ->
                super.onBackPressed()
            }.setNegativeButton("NO") { dialogInterface: DialogInterface?, i: Int ->
            }.show()
    }
}