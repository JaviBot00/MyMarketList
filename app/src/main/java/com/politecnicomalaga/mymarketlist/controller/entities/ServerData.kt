package com.politecnicomalaga.mymarketlist.controller.entities

import android.app.Activity
import android.widget.Toast
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.SQLite.MarketListSQLite
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest
import com.politecnicomalaga.mymarketlist.view.activity.LoginActivity
import com.politecnicomalaga.mymarketlist.view.activity.RegisterActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServerData(private val fromActivity: Activity) {
    private val DELIMITER1 = ";"
    private val DELIMITER2 = ","
    private val SELECT_TYPES_TABLES = "selectTypesTables.php"
    private val SELECT_PRODUCTS_FROM = "selectProductsFrom.php"
    private val SELECT_FROM_DATABASE = "selectFromDatabase.php"
    private val CREATE_DATABASE = "createDatabase.php"


    fun getServerProductTables() {
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", "HotGuy").build()
        MyRequest(fromActivity).phpQuery(SELECT_TYPES_TABLES, requestBody1) { response ->
            val tables = response.split(DELIMITER1)
            checkTables(tables)
            tables.forEach { t ->
                if (t.isNotEmpty()) {
                    val aux = t.split(DELIMITER2)
                    val requestBody2 = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("table", aux[1]).build()
                    MyRequest(fromActivity).phpQuery(
                        SELECT_PRODUCTS_FROM, requestBody2
                    ) { productsResponse ->
                        val products = productsResponse.split(DELIMITER1)
                        checkProducts(aux[1], products)
                    }
                }
            }
            println("kkk")
        }
    }

    fun getClientUser(username: String, password: String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username).build()
        MyRequest(fromActivity).phpQuery(
            SELECT_FROM_DATABASE, requestBody
        ) { response ->
            val values: List<String> = response.split(DELIMITER2)
            if (response.isNotEmpty() && (values[0] == username && values[1] == password)) {
                LoginActivity.getInstance().doAccess(fromActivity)
            } else {
                Toast.makeText(
                    fromActivity,
                    fromActivity.resources.getString(R.string.user_not_exist),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun setClientUser(username: String, password: String, email: String, imgProfile: ByteArray) {
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username).build()
        MyRequest(fromActivity).phpQuery(
            SELECT_FROM_DATABASE, requestBody1
        ) { response1 ->
            val values: List<String> = response1.split(DELIMITER2)
            if (response1.isNotEmpty() && (values[0] == username && values[1] == password)) {
                Toast.makeText(
                    fromActivity,
                    fromActivity.resources.getString(R.string.user_exist),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val requestBody2 = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("username", username).addFormDataPart("pass", password)
                    .addFormDataPart("email", email).addFormDataPart(
                        "imgProfile",
                        "image.png",
                        RequestBody.create("image/png".toMediaTypeOrNull(), imgProfile)
                    ).build()
                MyRequest(fromActivity).phpQuery(CREATE_DATABASE, requestBody2) { response2 ->
                    val state = response2.split(DELIMITER2)
                    if (state.all { it == "OK" }) {
                        RegisterActivity.getInstance().endRegister(fromActivity)
                    } else {
                        TODO()
                    }
                }
            }
        }
    }

    private fun checkTables(tCloud: List<String>) {
        val mySQLite = MarketListSQLite(fromActivity)
        mySQLite.setWritable()
        tCloud.forEach { t ->
            if (t.isNotEmpty()) {
                val aux = t.split(DELIMITER2)
                mySQLite.insertTables(aux[0], aux[1])
                mySQLite.createTable(aux[1])
            }
        }
        mySQLite.getDb().close()
    }

    private fun checkProducts(table: String, pCloud: List<String>) {
        val mySQLite = MarketListSQLite(fromActivity)
        mySQLite.setWritable()
        pCloud.forEach { p ->
            if (p.isNotEmpty()) {
                val aux = p.split(DELIMITER2)
                mySQLite.insertProduct(table, aux[0], aux[1])
            }
        }
        mySQLite.getDb().close()
    }
}
