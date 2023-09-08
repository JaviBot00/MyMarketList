package com.politecnicomalaga.mymarketlist.view.vActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.view.vFragments.Nav1ListsFragment
import com.politecnicomalaga.mymarketlist.view.vFragments.Nav2DetailsFragment

class ControlPanelActivity : AppCompatActivity() {

    companion object {
        const val CATALOGUE_REQUEST = 1000
        const val LIST_REQUEST = 2000
        const val STATS_REQUEST = 3000
        const val SUGGEST_REQUEST = 4000
    }

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_panel)
        MainController().setControllers(this@ControlPanelActivity, R.string.app_name, "")
        ServerData(this@ControlPanelActivity).updateServerLists()

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
}