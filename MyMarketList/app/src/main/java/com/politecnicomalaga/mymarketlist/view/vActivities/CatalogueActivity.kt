package com.politecnicomalaga.mymarketlist.view.vActivities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cAdapter.TabsVPAdapter
import com.politecnicomalaga.mymarketlist.controller.cEntities.ServerData
import com.politecnicomalaga.mymarketlist.controller.cSQLite.CatalogueSQLite
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite

class CatalogueActivity : AppCompatActivity() {

    companion object {
        private var myCatalogue: CatalogueActivity? = null

        fun getInstance(): CatalogueActivity {
            if (myCatalogue == null) {
                myCatalogue = CatalogueActivity()
            }
            return myCatalogue!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)
        MainController().setControllers(this@CatalogueActivity, R.string.catalogue, "")

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabsVPAdapter = TabsVPAdapter(
            supportFragmentManager,
            this@CatalogueActivity,
            CatalogueSQLite(this@CatalogueActivity).getCatalogue()
        )

        viewPager.adapter = tabsVPAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        val fabClearList: FloatingActionButton = findViewById(R.id.fabClearList)
        val fabShowItems: FloatingActionButton = findViewById(R.id.fabShowItems)
        val fabCreateList: FloatingActionButton = findViewById(R.id.fabCreateList)

        fabClearList.setOnClickListener {
            MaterialAlertDialogBuilder(this@CatalogueActivity).setTitle(resources.getString(R.string.clear_list))
                .setMessage(resources.getString(R.string.clear_list_message))
                .setNegativeButton(resources.getString(R.string.cancel), null)
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    ClientSQLite.myProductsList.clear()
                    tabsVPAdapter.notifyDataSetChanged()
                    viewPager.adapter = tabsVPAdapter
                }.setCancelable(false).show()
        }

        fabShowItems.setOnClickListener {
            MaterialAlertDialogBuilder(this@CatalogueActivity).setTitle(resources.getString(R.string.items_selected))
                .setItems(ClientSQLite.myProductsList.map { it.sName }.toTypedArray(), null)
                .setPositiveButton(resources.getString(R.string.accept), null).setCancelable(false)
                .setOnItemSelectedListener(null).show()
        }

        fabCreateList.setOnClickListener {
            if (!ClientSQLite.myProductsList.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(this@CatalogueActivity)
                val inflater = LayoutInflater.from(this@CatalogueActivity)
                val dialogView = inflater.inflate(R.layout.dialog_ly, null)
                val textInputList: TextInputLayout = dialogView.findViewById(R.id.editTxtList)

                builder.setView(dialogView).setTitle(resources.getString(R.string.catalogue))
                    .setMessage(resources.getString(R.string.make_list_name))
                    .setNegativeButton(resources.getString(R.string.cancel), null)
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                        if (!textInputList.editText!!.text.isNullOrEmpty()) {
                            ClientSQLite(this@CatalogueActivity).setList(textInputList.editText!!.text)
                            if (MainController().isConnected(this@CatalogueActivity)) {
                                ServerData(this@CatalogueActivity).updateServerLists()
                            }
                        } else {
                            MainController().showToast(
                                this@CatalogueActivity, R.string.please_put_name
                            )
                            dialog.dismiss()
                        }
                    }
                builder.create()
                builder.show()
            } else {
                MainController().showToast(this@CatalogueActivity, R.string.choose_item)
            }
        }
    }

    fun endCatalogue(fromActivity: Activity) {
        val result = Intent(fromActivity, ControlPanelActivity::class.java)
        fromActivity.setResult(RESULT_OK, result)
        fromActivity.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                MainController().exitDialog(this@CatalogueActivity)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}