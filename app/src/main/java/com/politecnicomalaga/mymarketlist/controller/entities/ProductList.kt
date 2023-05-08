package com.politecnicomalaga.mymarketlist.controller.entities

import MarketListSQLite
import android.app.Activity
import android.database.Cursor
import com.politecnicomalaga.mymarketlist.model.ClassifiedProducts
import com.politecnicomalaga.mymarketlist.model.Product

class ProductList private constructor() {
    private var myProducts = arrayListOf(ClassifiedProducts())

    companion object {
        val myList = arrayListOf<Product>()
        private var myProductList: ProductList? = null

        fun getInstance(): ProductList {
            if (myProductList == null) {
                myProductList = ProductList()
            }
            return myProductList!!
        }
    }

    private fun loadData(fromActivity: Activity) {
        myProducts.clear()
        val mySQLite = MarketListSQLite.getInstance(fromActivity)
        mySQLite.setWritable()
        val tc: Cursor = mySQLite.getTables()
        while (tc.moveToNext()) {
            if (tc.getString(0) != "android_metadata") {
                val type = ClassifiedProducts()
                type.category = tc.getString(0)
                if (tc.getString(0) != "android_metadata") {
                    val pc: Cursor = mySQLite.getProducts(tc.getString(0))
                    while (pc.moveToNext()) {
                        type.products += Product(pc.getString(0))
                    }
                }
                myProducts.add(type)
            }
        }
        mySQLite.getDb().close()
    }

    fun getMyProducts(fromActivity: Activity): ArrayList<ClassifiedProducts> {
        loadData(fromActivity)
        return myProducts
    }
}