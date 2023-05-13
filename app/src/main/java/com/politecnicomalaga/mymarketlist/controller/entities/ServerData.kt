package com.politecnicomalaga.mymarketlist.controller.entities

import MarketListSQLite
import android.app.Activity
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest

class ServerData(private val fromActivity: Activity) {
    private val DELIMITER1 = ";"
    private val DELIMITER2 = ","
    private val SELECT_TYPES_TABLES = "selectTypesTables.php"
    private val SELECT_PRODUCTS_FROM = "selectProductsFrom.php"

    private val mySQLite = MarketListSQLite(fromActivity)

    fun getServerProductTables() {
        mySQLite.setWritable()
        MyRequest(fromActivity).phpQuery(SELECT_TYPES_TABLES) { response ->
            val tables = response.split(DELIMITER1)
            checkTables(tables)
            tables.forEach { t ->
                if (t.isNotEmpty()) {
                    val aux = t.split(DELIMITER2)
                    MyRequest(fromActivity).phpQuery("$SELECT_PRODUCTS_FROM?table=" + aux[1]) { productsResponse ->
                        val products = productsResponse.split(DELIMITER1)
                        checkProducts(aux[1], products)
                    }
                }
            }
        }
    }

    private fun checkTables(tCloud: List<String>) {
        tCloud.forEach { t ->
            if (t.isNotEmpty()) {
                val aux = t.split(DELIMITER2)
                mySQLite.insertTables(aux[0], aux[1])
                mySQLite.createTable(aux[1])
            }
        }
    }

    private fun checkProducts(table: String, pCloud: List<String>) {
        pCloud.forEach { p ->
            if (p.isNotEmpty()) {
                val aux = p.split(DELIMITER2)
                mySQLite.insertProduct(table, aux[0], aux[1])
            }
        }
    }
}
