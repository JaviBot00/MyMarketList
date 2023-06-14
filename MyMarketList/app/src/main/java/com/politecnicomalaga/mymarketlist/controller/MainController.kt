package com.politecnicomalaga.mymarketlist.controller

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.media.Session2Command
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.cHTTP.MyRequest
import com.politecnicomalaga.mymarketlist.view.vActivities.CatalogueActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.EditActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.ListActivity
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

    fun setControllers(fromActivity: AppCompatActivity, title: Int, list: String) {
        setAppBar(fromActivity, title, list)
        backPressed(fromActivity)
    }

    fun showToast(fromActivity: Activity, message: Int) {
        if (message != 0) Toast.makeText(fromActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun exitDialog(fromActivity: AppCompatActivity){
        MaterialAlertDialogBuilder(fromActivity).setTitle(fromActivity.resources.getString(R.string.alert))
            .setMessage(fromActivity.resources.getString(R.string.exit_activity))
            .setPositiveButton(fromActivity.resources.getString(R.string.accept)) { _, _ ->
                fromActivity.setResult(RESULT_CANCELED)
                fromActivity.finish()
            }.setNegativeButton(
                fromActivity.resources.getString(R.string.cancel), null
            ).show()
    }

    private fun setAppBar(fromActivity: AppCompatActivity, title: Int, list: String) {
        MyRequest(fromActivity).checkNetworkConnectivity()
//        fromActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
//                    fromActivity, R.color.colorButton
//                )
//            )
//        )

        var spannableString = SpannableString(list)
        if (fromActivity !is EditActivity) {
            spannableString = SpannableString(fromActivity.resources.getString(title))
        }
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

    private fun backPressed(fromActivity: AppCompatActivity) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (fromActivity) {
                    is RegisterActivity, is CatalogueActivity, is EditActivity -> {
                        exitDialog(fromActivity)
                    }
                }
            }
        }
        fromActivity.onBackPressedDispatcher.addCallback(fromActivity, callback)
    }

    fun isConnected(fromActivity: Activity): Boolean {
        if (fromActivity.window.statusBarColor == ContextCompat.getColor(
                fromActivity, android.R.color.holo_green_dark
            )
        ) {
            return true
        } else {
            MainController().setColorStatusBar(fromActivity, TRY, false)
            MyRequest(fromActivity).checkNetworkConnectivity()
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
                if (fromActivity is ListActivity) {
                    ListActivity.getInstance().setRecycler(fromActivity)
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
                    showToast(fromActivity, R.string.try_again_later)
                    bOK = false
                    bTRY = true
                    bFAIL = false
                }
            }
        }

    }
}
