package com.politecnicomalaga.mymarketlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politecnicomalaga.mymarketlist.view.selectProducts.Frutas
import com.politecnicomalaga.mymarketlist.view.selectProducts.Verduras


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        loadFragment(Frutas())
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page1 -> {
                    loadFragment(Frutas())
                    it.icon =
                        AppCompatResources.getDrawable(this, R.drawable.ic_launcher_foreground)
                    true
                }

                R.id.page2 -> {
                    loadFragment(Verduras())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

}