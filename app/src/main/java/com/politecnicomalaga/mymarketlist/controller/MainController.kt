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
import com.politecnicomalaga.mymarketlist.view.vActivities.RegisterActivity

class MainController {

    companion object {
        const val OK = 1
        const val TRY = 0
        const val FAIL = -1
        var bOK = true
        var bTRY = true
        var bFAIL = true
    }

    fun setAppBar(fromActivity: AppCompatActivity, title: String) {
        MyRequest(fromActivity).checkNetworkConnectivity()
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
        if (message != 0) Toast.makeText(fromActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun isConnected(fromActivity: Activity): Boolean {
        if (fromActivity.window.statusBarColor == ContextCompat.getColor(
                fromActivity, android.R.color.holo_green_dark
            )
        ) {
            return true
        } else {
            MyRequest(fromActivity).checkNetworkConnectivity()
            showToast(fromActivity, R.string.try_agein_later)
        }
        return false
    }

    fun setColorStatusBar(fromActivity: Activity, ping: Int, b: Boolean) {
        when (ping) {
            OK -> {
                fromActivity.window.statusBarColor =
                    ContextCompat.getColor(fromActivity, android.R.color.holo_green_dark)
                if (bOK) {
                    showToast(fromActivity, R.string.successful_connection)
                    bOK = false
                    bTRY = false
                    bFAIL = true
                }
            }

            TRY -> {
                fromActivity.window.statusBarColor = ContextCompat.getColor(
                    fromActivity, android.R.color.holo_orange_light
                )
                if (bTRY) {
                    showToast(fromActivity, R.string.try_connection)
                    bOK = true
                    bTRY = false
                    bFAIL = true
                }
            }

            FAIL -> {
                fromActivity.window.statusBarColor =
                    ContextCompat.getColor(fromActivity, android.R.color.holo_red_dark)
                if (bFAIL) {
                    if (b) {
                        showToast(fromActivity, R.string.fail_connection)
                    } else {
                        showToast(fromActivity, R.string.error_network)
                    }
                    showToast(fromActivity, R.string.check_network)
                    bOK = true
                    bTRY = true
                    bFAIL = false
                }
            }
        }

    }
}
