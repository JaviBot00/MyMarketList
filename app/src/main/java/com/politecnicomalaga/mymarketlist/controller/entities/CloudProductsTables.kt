package com.politecnicomalaga.mymarketlist.controller.entities

import MarketListSQLite
import android.app.Activity
import android.database.Cursor
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest

class CloudProductsTables(val fromActivity: Activity) {

    companion object {
        private const val DELIMITER = ";"
        private const val SHOW_TABLE = "showTables.php"
        private const val SELECT_PRODUCTS_FROM = "selectProductsFrom.php"
    }

    private val mySQLite = MarketListSQLite.getInstance(fromActivity)

    fun getCloudProductTables() {
        mySQLite.setWritable() // Sets the database to be writable
        MyRequest(fromActivity).phpQuery(SHOW_TABLE, // Sends a PHP query to retrieve a list of all tables from the server
            object : MyRequest.ResponseListener {
                override fun onResponseReceived(response: String) {
                    val tables =
                        response.split(DELIMITER) // Splits the response into a list of table names
                    checkTables(tables) // Calls the checkTables function to compare server tables with local tables

                    // For each table in the server, retrieves all products and calls checkProducts to compare them with local products
                    tables.forEach { t ->
                        if (t.isNotEmpty()) {
                            MyRequest(fromActivity).phpQuery("$SELECT_PRODUCTS_FROM?table=$t",
                                object : MyRequest.ResponseListener {
                                    override fun onResponseReceived(response: String) {
                                        val products =
                                            response.split(DELIMITER) // Splits the response into a list of products
                                        checkProducts(
                                            t, products
                                        ) // Calls the checkProducts function to compare server products with local products
                                    }
                                })
                        }
                    }
//                    mySQLite.getDb().close() // Close database
                }
            })
    }


    fun checkTables(tCloud: List<String>) {
        val tLocal: Cursor = mySQLite.getTables()

        // Create any table in the cloud that does not exist in the local database
        tCloud.forEach { t ->
            if (t.isNotEmpty()) { // If the table does not exist in the local database
                mySQLite.createTable(t)
            }
        }

        // Delete any table in the local database that does not exist in the cloud
        while (tLocal.moveToNext()) {
            val tableName = tLocal.getString(0)
            if (tableName != "android_metadata" && !tCloud.contains(tableName)) {
                mySQLite.deleteTable(tableName)
            }
        }
    }

    fun checkProducts(table: String, pCloud: List<String>) {
        val pLocal: Cursor = mySQLite.getProducts(table)
        pCloud.forEach { p ->
            if (p.isNotEmpty()) { // Create any product in the cloud that doesn't exist locally
//                if (!cursorContainsProduct(pLocal, p)) {
                mySQLite.insertProduct(table, p)
//                }
            }
        }

        while (pLocal.moveToNext()) {
            val productName = pLocal.getString(Integer.parseInt(MarketListSQLite.PRODUCT_ES[1]))
            if (!pCloud.contains(productName)) { // Delete local product if it's not present in pCloud
                mySQLite.deleteProduct(table, productName)
            }
        }
    }

//    // Helper function to check if a product is already in the cursor
//    private fun cursorContainsProduct(cursor: Cursor, productName: String): Boolean {
//        cursor.moveToFirst()
//        while (!cursor.isAfterLast) {
//            if (cursor.getString(Integer.parseInt(MarketListSQLite.TPRODUCTS[1])) == productName) {
//                return true
//            }
//            cursor.moveToNext()
//        }
//        return false
//    }

}