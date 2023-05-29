package com.politecnicomalaga.mymarketlist.controller.entities

import android.app.Activity
import android.content.ContentValues
import android.database.SQLException
import android.util.Base64
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.SQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.controller.SQLite.MarketListSQLite
import com.politecnicomalaga.mymarketlist.controller.http.MyRequest
import com.politecnicomalaga.mymarketlist.view.activity.LoginActivity
import com.politecnicomalaga.mymarketlist.view.activity.RegisterActivity
import okhttp3.MultipartBody

class ServerData(private val fromActivity: Activity) {
    private val DELIMITER1 = ";"
    private val DELIMITER2 = ","
    private val SELECT_TYPES_TABLES = "selectTypesTables.php"
    private val SELECT_PRODUCTS_FROM = "selectProductsFrom.php"
    private val SELECT_FROM_DATABASE = "selectFromDatabase.php"
    private val SELECT_FROM_DATABASE_IMAGE = "selectFromDatabaseImage.php"
    private val CREATE_DATABASE = "createDatabase.php"


    fun getServerProductTables() {
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", "HotGuy").build()
        MyRequest(fromActivity).phpQuery(SELECT_TYPES_TABLES, requestBody1, { tableResponse ->
            val tables = tableResponse.split(DELIMITER1)
            checkTables(tables)
            tables.forEach { t ->
                if (t.isNotEmpty()) {
                    val aux = t.split(DELIMITER2)
                    val requestBody2 = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("table", aux[1]).build()
                    MyRequest(fromActivity).phpQuery(SELECT_PRODUCTS_FROM,
                        requestBody2,
                        { productsResponse ->
                            val products = productsResponse.split(DELIMITER1)
                            checkProducts(aux[1], products)
                        },
                        {})
                }
            }
        }, {})
    }

    fun getServerUser(username: String, password: String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username).build()
        MyRequest(fromActivity).phpQuery(SELECT_FROM_DATABASE, requestBody, { userResponse ->
            val loginValues: List<String> = userResponse.split(DELIMITER2)
            if (userResponse.isNotEmpty() && (loginValues[0] == username && loginValues[1] == password)) {
//                MyRequest(fromActivity).phpQuery(SELECT_FROM_DATABASE_IMAGE,
//                    requestBody,
//                    { imageResponse ->
//                        val imgProfile: ByteArray = Base64.decode(imageResponse, Base64.DEFAULT);
//                        if (imageResponse.isNotEmpty()) {
//                            setUser(
//                                loginValues[0], loginValues[1], loginValues[2], imgProfile
//                            )
                LoginActivity.getInstance().doAccess(fromActivity)
//                        } else {
//                            LoginActivity.getInstance()
//                                .setError(fromActivity, R.string.error_get_user_data)
//                        }
//                    },
//                    { LoginActivity.getInstance().setError(fromActivity, 0) })
            } else {
                LoginActivity.getInstance().setError(fromActivity, R.string.user_not_exist)
            }
        }, {})
    }

    fun setServerUser(username: String, password: String, email: String, imgProfile: ByteArray) {
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username).build()
        MyRequest(fromActivity).phpQuery(SELECT_FROM_DATABASE, requestBody1, { response1 ->
            val loginValues: List<String> = response1.split(DELIMITER2)
            if (response1.isNotEmpty() && (loginValues[0] == username && loginValues[1] == password)) {
                MainController().showToast(fromActivity, R.string.user_exist)
            } else {
                val requestBody2 = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("username", username).addFormDataPart("pass", password)
                    .addFormDataPart("email", email).addFormDataPart(
                        "imgProfile", Base64.encodeToString(imgProfile, Base64.DEFAULT)
                    ).build()
                MyRequest(fromActivity).phpQuery(CREATE_DATABASE, requestBody2, { response2 ->
                    val state = response2.split(DELIMITER2)
                    if (state.all { it == "OK" }) {
                        setUser(username, password, email, imgProfile)
                    } else {
                        RegisterActivity.getInstance().setError(fromActivity)
                    }
                }, { RegisterActivity.getInstance().setError(fromActivity) })
            }
        }, { RegisterActivity.getInstance().setError(fromActivity) })
    }

    fun getServerLists(){}
    fun setServerLists(){}

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

    private fun setUser(username: String, password: String, email: String, imgProfile: ByteArray) {
        try {
            val mySQLite = ClientSQLite(fromActivity)
            mySQLite.setWritable()
            val userValues = ContentValues()
            userValues.put(
                ClientSQLite.TUsers_USER[0], username
            )
            userValues.put(
                ClientSQLite.TUsers_PASSWORD[0], password
            )
            userValues.put(
                ClientSQLite.TUsers_EMAIL[0], email
            )
            userValues.put(ClientSQLite.TUsers_IMGPROFILE[0], imgProfile)
            mySQLite.insertUser(userValues)
            mySQLite.getDb().close()
            RegisterActivity.getInstance().endRegister(fromActivity)
        } catch (e: SQLException) {
            MainController().showToast(fromActivity, R.string.error_at_insert_data)
        }
    }
}
