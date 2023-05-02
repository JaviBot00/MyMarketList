package com.politecnicomalaga.mymarketlist

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politecnicomalaga.mymarketlist.controller.entities.CloudProductsTables
import com.politecnicomalaga.mymarketlist.view.mianMenu.Page1
import com.politecnicomalaga.mymarketlist.view.mianMenu.Page2


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen()
        Toast.makeText(this@MainActivity, this@MainActivity.getString(R.string.checking_backup), Toast.LENGTH_LONG).show()
        try {
//            val mySQLite = ManagerSQLite(this@MainActivity)
//            mySQLite.setWritable()
//            mySQLite.setReadable()
            CloudProductsTables(this@MainActivity).getCloudProductTables()
        }catch (e: Exception){
            println(e.message)
            Toast.makeText(this@MainActivity, this@MainActivity.getString(R.string.error_backup), Toast.LENGTH_LONG).show()
        }
        setContentView(R.layout.activity_main)
        setAppBar()



        loadFragment(Page1(this@MainActivity))
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page1 -> {
                    loadFragment(Page1(this@MainActivity))
//                    it.icon =
//                        AppCompatResources.getDrawable(this, R.drawable.ic_launcher_foreground)
                    true
                }

                R.id.page2 -> {
                    loadFragment(Page2())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    private fun setAppBar(){
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.marine_blue)))
        val spannableString = SpannableString(resources.getString(R.string.app_name))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = spannableString
    }

}