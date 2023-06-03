package com.politecnicomalaga.mymarketlist.controller

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.cHTTP.MyRequest
import com.politecnicomalaga.mymarketlist.view.vActivities.LoginActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.RegisterActivity

class MainController {

    // Metodos estaticos para los colores

//    companion object {
//        private var myController: MainController? = null
//
//        fun getInstance(): MainController {
//            if (myController == null) {
//                myController = MainController()
//            }
//            return myController!!
//        }
//    }

    fun setAppBar(fromActivity: AppCompatActivity, title: String) {
        MyRequest(fromActivity).checkNetworkConnectivity()
        if (fromActivity.window.statusBarColor == ContextCompat.getColor(
                fromActivity, R.color.colorAccent
            )
        ) {
            MainController().showToast(fromActivity, R.string.try_connection)
        }

        fromActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (fromActivity.supportActionBar != null) {
            if (fromActivity.parentActivityIntent == null) {
                fromActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                fromActivity.supportActionBar!!.setDisplayShowHomeEnabled(false)
            } else {
                fromActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                fromActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }

//        fromActivity.supportActionBar!!.setBackgroundDrawable(
//            ColorDrawable(
//                ContextCompat.getColor(
//                    fromActivity,
//                    R.color.marine_blue
//                )
//            )
//        )

        val spannableString = SpannableString(title)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        fromActivity.supportActionBar?.title = spannableString
    }

    fun backPressed(fromActivity: AppCompatActivity) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (fromActivity) {
                    is RegisterActivity -> {
                        MaterialAlertDialogBuilder(fromActivity).setTitle("Warning")
                            .setMessage(fromActivity.resources.getString(R.string.exit_register_activity))
                            .setPositiveButton(fromActivity.resources.getString(R.string.accept)) { _, _ ->
                                fromActivity.finish()
                            }.setNegativeButton(
                                fromActivity.resources.getString(R.string.cancel), null
                            ).show()
                    }
                }
            }
        }
        fromActivity.onBackPressedDispatcher.addCallback(fromActivity, callback)
    }

    fun showToast(fromActivity: Activity, message: Int) {
        fromActivity.runOnUiThread {
            if (message != 0) Toast.makeText(fromActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setColorStatusBar(fromActivity: Activity, ping: Boolean) {
        if (ping) {
            fromActivity.window.statusBarColor =
                ContextCompat.getColor(fromActivity, android.R.color.holo_green_dark)
            MainController().showToast(fromActivity, R.string.successful_connection)
        } else {
            fromActivity.window.statusBarColor =
                ContextCompat.getColor(fromActivity, R.color.colorPrimary)
            MainController().showToast(fromActivity, R.string.error_network)
            MainController().showToast(fromActivity, R.string.check_network)
        }

        if (fromActivity is LoginActivity) {
            LoginActivity.getInstance().setEnable(fromActivity, ping)
        }
    }
}
