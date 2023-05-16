package com.politecnicomalaga.mymarketlist.view.activity

import com.politecnicomalaga.mymarketlist.controller.SQLite.ClientSQLite
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.adapter.TabsVPAdapter
import com.politecnicomalaga.mymarketlist.controller.entities.ProductList

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        MainController().setAppBar(this@ProductsActivity, resources.getString(R.string.make_list))
        ProductList.myList.clear()

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabsVPAdapter = TabsVPAdapter(
            supportFragmentManager, ProductList().getMyProducts(this@ProductsActivity)
        )

        viewPager.adapter = tabsVPAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        val fabClearList: FloatingActionButton = findViewById(R.id.fabClearList)
        val fabShowItems: FloatingActionButton = findViewById(R.id.fabShowItems)
        val fabCreateList: FloatingActionButton = findViewById(R.id.fabCreateList)

        fabClearList.setOnClickListener {
            MaterialAlertDialogBuilder(this@ProductsActivity).setTitle(resources.getString(R.string.clear_list))
                .setMessage(resources.getString(R.string.clear_list_message))
                .setNegativeButton(resources.getString(R.string.cancel), null)
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    ProductList.myList.clear()
                    tabsVPAdapter.notifyDataSetChanged()
                    viewPager.adapter = tabsVPAdapter
                }.setCancelable(false).show()
        }

        fabShowItems.setOnClickListener {
            MaterialAlertDialogBuilder(this@ProductsActivity).setTitle(resources.getString(R.string.items_selected))
                .setItems(ProductList.myList.map { it.name }.toTypedArray(), null)
                .setPositiveButton(resources.getString(R.string.accept), null).setCancelable(false)
                .setOnItemSelectedListener(null).show()
        }

        fabCreateList.setOnClickListener {
            if (!ProductList.myList.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(this@ProductsActivity)
                val inflater = LayoutInflater.from(this@ProductsActivity)
                val dialogView = inflater.inflate(R.layout.dialog_ly, null)
                val textInputList: TextInputLayout = dialogView.findViewById(R.id.editTxtList)

                builder.setView(dialogView).setTitle(resources.getString(R.string.make_list))
                    .setMessage(resources.getString(R.string.make_list_name))
                    .setNegativeButton(resources.getString(R.string.cancel), null)
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                        if (!textInputList.editText!!.text.isNullOrEmpty()) {
                            val mySQLite = ClientSQLite(this@ProductsActivity)
                            mySQLite.setWritable()
                            mySQLite.setReadable()
                            val txtReplace: String =
                                textInputList.editText!!.text.toString().replace(" ", "_", false)
                            mySQLite.insertList(txtReplace)
                            mySQLite.createTable(txtReplace)
                            ProductList.myList.forEach {
                                mySQLite.insertProduct(
                                    txtReplace, it.name
                                )
                            }
                            mySQLite.getDb().close()
                            finish()
                        } else {
                            Toast.makeText(
                                this@ProductsActivity,
                                getString(R.string.please_put_name),
                                Toast.LENGTH_LONG
                            ).show()
                        dialog.dismiss()
                        }
                    }
                        builder.create()
                        builder.show()
            } else {
                Toast.makeText(
                    this@ProductsActivity,
                    getString(R.string.choose_item),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

//        val builder = MaterialAlertDialogBuilder(context)
//        builder.setTitle("Mi diálogo")
//        builder.setMessage("Este es el mensaje del diálogo.")
//        builder.setPositiveButton("Aceptar") { dialog, which ->
//            // Acción a realizar al hacer clic en el botón Aceptar
//        }
//
//        val textView = builder.show().findViewById<TextView>(android.R.id.message)
//        textView.textSize = 20f // Tamaño de fuente en píxeles

    }

}