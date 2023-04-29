package com.politecnicomalaga.mymarketlist.controller.entities

import android.app.Activity
import android.database.Cursor
import com.google.android.material.snackbar.Snackbar
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.adapter.ManagerSQLite
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class CloudProductsTables(val fromActivity: Activity) {

    companion object {
        private const val DELIMITER_TABLE = "T"
        private const val DELIMITER_PRODUCTS = ";"
        private const val SHOW_TABLE = "showTables.php"
        private const val SELECT_PRODUCTS_FROM = "selectProductsFrom.php"
    }
    //    private val myProductsList = sortedMapOf<String, SortedSet<String>>()

    fun getCloudProductTables() {
        val mySQLite = ManagerSQLite(fromActivity)
        mySQLite.setWritable()
        mySQLite.setReadable()
        val tc: Cursor = mySQLite.getTables()
        MyRequest().phpQuery(SHOW_TABLE, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.body!!.string().split(DELIMITER_TABLE).forEach { t ->
                    if (t == tc.getString(0) && !tc.getString(0).isNullOrEmpty()) {
                        tc.moveToNext()
                    } else {
                        mySQLite.createTable(t)
                    }
                    val pc: Cursor = mySQLite.getProducts(t)
                    MyRequest().phpQuery("$SELECT_PRODUCTS_FROM?table=$t", object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            response.body!!.string().split(DELIMITER_PRODUCTS).forEach { p ->
                                if (p == pc.getString(0) && !pc.getString(0).isNullOrEmpty()) {
                                    pc.moveToNext()
                                } else {
                                    mySQLite.insertProduct(t, p)
                                }
                            }
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }
                    })
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Snackbar.make(
                    fromActivity.findViewById(android.R.id.content),
                    fromActivity.getString(R.string.host_unreachable),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

//    fun getCloudProduct(): SortedMap<String, SortedSet<String>> {
//        val productsList = sortedMapOf<String, SortedSet<String>>()
//        val tables: String = getCloudTables()
//        tables.split("T").forEach { t ->
//            val products = sortedSetOf<String>()
//            MyRequest().phpQuery(
//                "$SELECT_PRODUCTS_FROM?table=$t",
//                object : Callback {
//                    override fun onResponse(call: Call, response: Response) {
//
//                        if (t.isNotEmpty()) {
//                            products.add(response.body!!.string())
//                        }
//                    }
//
//                    override fun onFailure(call: Call, e: IOException) {
//                        e.printStackTrace()
//                    }
//                })
//            if (t.isNotEmpty()) {
//                productsList[t] = products
//            }
//        }
//        return productsList
//    }
}