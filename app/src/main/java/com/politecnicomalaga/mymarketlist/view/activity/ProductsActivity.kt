package com.politecnicomalaga.mymarketlist.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.adapter.TabsVPAdapter
import com.politecnicomalaga.mymarketlist.controller.entities.ProductList

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        MainController.getInstance()
            .setAppBar(this@ProductsActivity, resources.getString(R.string.make_list))
        ProductList.myList.clear()

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabsVPAdapter = TabsVPAdapter(
            supportFragmentManager,
            this@ProductsActivity,
            ProductList.getInstance().getMyProducts(this@ProductsActivity)
        )

        viewPager.adapter = tabsVPAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        val fabClearList: FloatingActionButton = findViewById(R.id.fabClearList)
        val fabShowItems: FloatingActionButton = findViewById(R.id.fabShowItems)
        val fabCreateList: FloatingActionButton = findViewById(R.id.fabCreateList)

        fabClearList.setOnClickListener { fab ->
            MaterialAlertDialogBuilder(fab.context).setTitle(resources.getString(R.string.clear_list)).setMessage(resources.getString(R.string.clear_list_message))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                }.setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    ProductList.myList.clear()
                    tabsVPAdapter.notifyDataSetChanged()
                    viewPager.adapter = tabsVPAdapter
                }.show()
        }

        fabShowItems.setOnClickListener { fab ->
            MaterialAlertDialogBuilder(fab.context).setTitle(resources.getString(R.string.items_selected))
                .setItems(ProductList.myList.map { it.name }.toTypedArray()) { dialog, which ->
                }.setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                }.setCancelable(false).show()
        }
    }

}