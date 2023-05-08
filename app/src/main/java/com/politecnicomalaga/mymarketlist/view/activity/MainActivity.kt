package com.politecnicomalaga.mymarketlist.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.entities.CloudProductsTables
import com.politecnicomalaga.mymarketlist.view.fragments.Menu1Fragment
import com.politecnicomalaga.mymarketlist.view.fragments.Menu2Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(
            this@MainActivity,
            this@MainActivity.getString(R.string.checking_backup),
            Toast.LENGTH_LONG
        ).show()
        try {
            CloudProductsTables(this@MainActivity).getCloudProductTables()
        } catch (e: Exception) {
            Toast.makeText(
                this@MainActivity,
                this@MainActivity.getString(R.string.error_backup),
                Toast.LENGTH_LONG
            ).show()
        }

        installSplashScreen()
        setContentView(R.layout.activity_main)
        MainController.getInstance()
            .setAppBar(this@MainActivity, resources.getString(R.string.app_name))

        loadFragment(Menu1Fragment(this@MainActivity))
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page1 -> {
                    loadFragment(Menu1Fragment(this@MainActivity))
//                    it.icon =
//                        AppCompatResources.getDrawable(this, R.drawable.ic_launcher_foreground)
                    true
                }

                R.id.page2 -> {
                    loadFragment(Menu2Fragment(this@MainActivity))
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

}