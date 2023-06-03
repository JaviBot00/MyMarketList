package com.politecnicomalaga.mymarketlist.view.vActivities

import android.os.Bundle
import android.view.LayoutInflater
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
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.controller.cSQLite.CatalogueSQLite

class CatalogueActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        MainController().setAppBar(this@CatalogueActivity, resources.getString(R.string.make_list))

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabsVPAdapter = TabsVPAdapter(
            supportFragmentManager, CatalogueSQLite(this@CatalogueActivity).getCatalogue()
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
                    ClientSQLite.myList.clear()
                    tabsVPAdapter.notifyDataSetChanged()
                    viewPager.adapter = tabsVPAdapter
                }.setCancelable(false).show()
        }

        fabShowItems.setOnClickListener {
            MaterialAlertDialogBuilder(this@CatalogueActivity).setTitle(resources.getString(R.string.items_selected))
                .setItems(ClientSQLite.myList.map { it.sName }.toTypedArray(), null)
                .setPositiveButton(resources.getString(R.string.accept), null).setCancelable(false)
                .setOnItemSelectedListener(null).show()
        }

        fabCreateList.setOnClickListener {
            if (!ClientSQLite.myList.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(this@CatalogueActivity)
                val inflater = LayoutInflater.from(this@CatalogueActivity)
                val dialogView = inflater.inflate(R.layout.dialog_ly, null)
                val textInputList: TextInputLayout = dialogView.findViewById(R.id.editTxtList)
//                textInputList.editText!!.textSize = 20f

                builder.setView(dialogView).setTitle(resources.getString(R.string.make_list))
                    .setMessage(resources.getString(R.string.make_list_name))
                    .setNegativeButton(resources.getString(R.string.cancel), null)
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                        if (!textInputList.editText!!.text.isNullOrEmpty()) {
                            ClientSQLite(this@CatalogueActivity).setList(textInputList.editText!!.text)
                            ServerData(this@CatalogueActivity).setServerLists()
                            finish()
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
}