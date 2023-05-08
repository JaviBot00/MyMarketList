package com.politecnicomalaga.mymarketlist.controller

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.politecnicomalaga.mymarketlist.R

class MainController {

    companion object {
        private var myController: MainController? = null

        fun getInstance(): MainController {
            if (myController == null) {
                myController = MainController()
            }
            return myController!!
        }
    }

    fun setAppBar(fromActivity: AppCompatActivity, title: String) {
        if (fromActivity.supportActionBar != null) {
            if (fromActivity.parentActivityIntent == null) {
                fromActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                fromActivity.supportActionBar!!.setDisplayShowHomeEnabled(false)
            } else {
                fromActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                fromActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }

        fromActivity.supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    fromActivity,
                    R.color.marine_blue
                )
            )
        )
        val spannableString = SpannableString(title)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        fromActivity.supportActionBar?.title = spannableString
    }
}
