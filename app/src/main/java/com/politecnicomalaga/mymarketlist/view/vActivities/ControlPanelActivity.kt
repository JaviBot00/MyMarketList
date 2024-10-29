package com.politecnicomalaga.mymarketlist.view.vActivities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.view.vFragments.Nav1ListsFragment
import com.politecnicomalaga.mymarketlist.view.vFragments.Nav2DetailsFragment


class ControlPanelActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val CATALOGUE_REQUEST = 1000
        const val LIST_REQUEST = 2000
        const val STATS_REQUEST = 3000
        const val SUGGEST_REQUEST = 4000
    }

    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private val navigationView: NavigationView? = null
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_panel)
        MainController().setControllers(this@ControlPanelActivity, R.string.app_name, "")
        ServerData(this@ControlPanelActivity).updateServerLists()

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mDrawerToggle = ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar, R.string.app_name, R.string.in_maintenance
        )
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)
        mDrawerToggle!!.syncState()


        loadFragment(Nav1ListsFragment(this@ControlPanelActivity))
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1_lists -> {
                    loadFragment(Nav1ListsFragment(this@ControlPanelActivity))
                    true
                }

                R.id.nav2_details -> {
                    loadFragment(Nav2DetailsFragment(this@ControlPanelActivity))
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
                            this@ControlPanelActivity, R.string.successful_create_list
                        )
                    }

                    RESULT_CANCELED -> {
                        MainController().showToast(
                            this@ControlPanelActivity, R.string.error_create_list
                        )
                    }
                }
            }

            SUGGEST_REQUEST -> {
                when (resultCode) {
                    RESULT_OK -> {
                        MainController().showToast(
                            this@ControlPanelActivity, R.string.successful_send_suggest
                        )
                    }

                    RESULT_CANCELED -> {
                        MainController().showToast(
                            this@ControlPanelActivity, R.string.error_send_suggest
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