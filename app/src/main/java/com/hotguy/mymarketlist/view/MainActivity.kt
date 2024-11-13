package com.hotguy.mymarketlist.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.hotguy.mymarketlist.controller.MainController
import com.hotguy.mymarketlist.view.vFragments.Nav1ListsFragment
import com.hotguy.mymarketlist.view.vFragments.Nav2DetailsFragment


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val CATALOGUE_REQUEST = 1000
        const val LIST_REQUEST = 2000
        const val STATS_REQUEST = 3000
        const val SUGGEST_REQUEST = 4000
    }

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainController().setControllers(this@MainActivity, R.string.app_name, "")
//        ServerData(this@MainActivity).updateServerLists()

        val toolbar: Toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mDrawerToggle = ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar, R.string.app_name, R.string.in_maintenance
        )
        mDrawerLayout.setDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.findItem(R.id.registrarTarjeta).setVisible(false)

//        //poner en el menu el usuario
//        val headerView = navigationView.getHeaderView(0)
//        val usuarioConectado_tv = headerView.findViewById<View>(R.id.usuario_conectado) as TextView
//        val usuarioConectado =
//            getSharedPreferences("preferences", MODE_PRIVATE).getString("usuario", null)
//        usuarioConectado_tv.text = usuarioConectado
//
//
//        //poner version en el menu
//        try {
//            val conetenidoMenu = navigationView.rootView
//            val version_input = conetenidoMenu.findViewById<View>(R.id.version) as TextView
//            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
//            version_input.text = "V.$versionName"
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }

        loadFragment(Nav1ListsFragment(this@MainActivity))
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1_lists -> {
                    loadFragment(Nav1ListsFragment(this@MainActivity))
                    true
                }

                R.id.nav2_details -> {
                    loadFragment(Nav2DetailsFragment(this@MainActivity))
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CATALOGUE_REQUEST -> {
                when (resultCode) {
                    RESULT_OK -> {
                        MainController().showToast(
                            this@MainActivity, R.string.successful_create_list
                        )
                    }

                    RESULT_CANCELED -> {
                        MainController().showToast(
                            this@MainActivity, R.string.error_create_list
                        )
                    }
                }
            }

            SUGGEST_REQUEST -> {
                when (resultCode) {
                    RESULT_OK -> {
                        MainController().showToast(
                            this@MainActivity, R.string.successful_send_suggest
                        )
                    }

                    RESULT_CANCELED -> {
                        MainController().showToast(
                            this@MainActivity, R.string.error_send_suggest
                        )
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}