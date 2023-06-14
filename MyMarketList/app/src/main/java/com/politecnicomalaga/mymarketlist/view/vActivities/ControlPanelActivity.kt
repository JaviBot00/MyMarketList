package com.politecnicomalaga.mymarketlist.view.vActivities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.view.vFragments.Nav1ListsFragment
import com.politecnicomalaga.mymarketlist.view.vFragments.Nav2DetailsFragment

class ControlPanelActivity : AppCompatActivity() {

    companion object {
        val CATALOGUE_REQUEST = 1000
        val LIST_REQUEST = 2000
        val STATS_REQUEST = 3000
    }

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_panel)
        MainController().setControllers(this@ControlPanelActivity, R.string.app_name, "")

        loadFragment(Nav1ListsFragment(this@ControlPanelActivity))
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1_lists -> {
                    loadFragment(Nav1ListsFragment(this@ControlPanelActivity))
                    true
                }

                R.id.nav2_details -> {
                    loadFragment(Nav2DetailsFragment())
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this@ControlPanelActivity).inflate(R.menu.menu_options, menu)
        menu.findItem(R.id.menu_log_out).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_out -> {
                ClientSQLite(this@ControlPanelActivity).resetTables(
                    ClientSQLite(this@ControlPanelActivity).writableDatabase, true, true
                )
                val result = Intent(this@ControlPanelActivity, LoginActivity::class.java)
                setResult(RESULT_OK, result)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}