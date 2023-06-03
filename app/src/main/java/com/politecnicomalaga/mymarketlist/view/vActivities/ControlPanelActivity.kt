package com.politecnicomalaga.mymarketlist.view.vActivities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.view.vFragments.Menu1Fragment
import com.politecnicomalaga.mymarketlist.view.vFragments.Menu2Fragment

class ControlPanelActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_control_panel)
        MainController().setAppBar(
            this@ControlPanelActivity, resources.getString(R.string.app_name)
        )
//        MainController().showToast(this@ControlPanelActivity, R.string.checking_backup)
//        ServerData(this@ControlPanelActivity).getServerProductTables()

        loadFragment(Menu1Fragment(this@ControlPanelActivity))
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu1 -> {
                    loadFragment(Menu1Fragment(this@ControlPanelActivity))
//                    it.icon =
//                        AppCompatResources.getDrawable(this, R.drawable.ic_launcher_foreground)
                    true
                }

                R.id.menu2 -> {
                    loadFragment(Menu2Fragment())
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this@ControlPanelActivity).inflate(R.menu.menu_options, menu)
        menu.findItem(R.id.menu_log_out)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_out -> {
                val mySQLite = ClientSQLite(this@ControlPanelActivity)
                mySQLite.delUser()
                val result = Intent(this@ControlPanelActivity, LoginActivity::class.java)
                setResult(RESULT_OK, result)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}