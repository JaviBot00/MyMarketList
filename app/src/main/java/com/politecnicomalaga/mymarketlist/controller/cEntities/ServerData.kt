package com.politecnicomalaga.mymarketlist.controller.cEntities

import android.app.Activity
import android.database.SQLException
import android.util.Base64
import android.util.Log
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cHTTP.MyRequest
import com.politecnicomalaga.mymarketlist.controller.cSQLite.CatalogueSQLite
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
import com.politecnicomalaga.mymarketlist.model.Lists
import com.politecnicomalaga.mymarketlist.model.Product
import com.politecnicomalaga.mymarketlist.model.UserFeatures
import com.politecnicomalaga.mymarketlist.view.vActivities.LoginActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.RegisterActivity
import okhttp3.MultipartBody

class ServerData(private val fromActivity: Activity) {

    private val DELIMITER1 = ";"
    private val DELIMITER2 = ","
    private val SELECT_FROM_TYPES = "selectFromTypes.php"
    private val SELECT_FROM_PRODUCTS = "selectFromProducts.php"
    private val SELECT_FROM_DATABASE = "selectFromDatabase.php"
    private val SELECT_FROM_DATABASE_IMAGE = "selectFromDatabaseImage.php"
    private val CREATE_DATABASE = "createDatabase.php"
    private val INSERT_LIST_INTO_DB = "insertListIntoDB.php"
    private val SELECT_LIST_FROM_DB = "selectListFromDB.php"
    private val SELECT_PRODUCTS_FROM_DB = "selectProductsFromDB.php"

    fun getServerProductTables() {
        MyRequest(fromActivity).phpQuery(SELECT_FROM_TYPES) { response1 ->
            response1.split(DELIMITER1).forEach {
                if (it.isNotEmpty()) {
                    CatalogueSQLite(fromActivity).setTypes(it.split(DELIMITER2))
                }
            }
            MyRequest(fromActivity).phpQuery(SELECT_FROM_PRODUCTS) { response2 ->
                response2.split(DELIMITER1).forEach {
                    if (it.isNotEmpty()) {
                        CatalogueSQLite(fromActivity).setItems(it.split(DELIMITER2))
                    }
                }
            }
        }
    }

    fun getServerUser(username: String, pass: String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username).addFormDataPart("pass", pass).build()
        MyRequest(fromActivity).phpQuery(SELECT_FROM_DATABASE, requestBody) { userResponse ->
            if (userResponse.isNotEmpty()) {
                setUser(
                    UserFeatures(
                        userResponse.split(DELIMITER2)[0],
                        userResponse.split(DELIMITER2)[1],
                        userResponse.split(DELIMITER2)[2],
                        null,
                        userResponse.split(DELIMITER2)[3]
                    ), false
                )
            } else {
                MainController().showToast(fromActivity, R.string.user_not_exist)
            }
        }
    }

    fun setServerUser(user: UserFeatures) {
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", user.userName).addFormDataPart("pass", user.passWord)
            .build()
        MyRequest(fromActivity).phpQuery(SELECT_FROM_DATABASE, requestBody1) { response1 ->
            if (response1.isNotEmpty()) {
                MainController().showToast(fromActivity, R.string.user_exist)
            } else {
                val requestBody2 = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("username", user.userName)
                    .addFormDataPart("pass", user.passWord).addFormDataPart("email", user.email)
                    .addFormDataPart(
                        "imgProfile",
                        Base64.encodeToString(user.imgProfile, Base64.DEFAULT) // En Base64
//                    ).addFormDataPart(
//                        "imgProfile", "image.png", imgProfile.toRequestBody(
//                            "application/octet-stream".toMediaTypeOrNull(), 0, imgProfile.size
//                        ) // En ByteArray
                    ).build()

                MyRequest(fromActivity).phpQuery(CREATE_DATABASE, requestBody2) { response2 ->
                    val state = response2.split(DELIMITER2)
                    if (state.all { it == "OK" }) {
                        setUser(user, true)
                    } else {
                        MainController().showToast(fromActivity, R.string.error_create_user)
                    }
                }
            }
        }
    }

    fun getServerLists() {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", ClientSQLite(fromActivity).getUser().userName).build()
        MyRequest(fromActivity).phpQuery(SELECT_LIST_FROM_DB, requestBody) { response1 ->
            ClientSQLite(fromActivity).resetTables(ClientSQLite(fromActivity).writableDatabase, true, false)

            response1.split(DELIMITER1).forEach {
                if (it.isNotEmpty()) {
                    ClientSQLite(fromActivity).setOnlineList(it.split(DELIMITER2))
                }
            }
            MyRequest(fromActivity).phpQuery(SELECT_PRODUCTS_FROM_DB, requestBody) { response2 ->
                response2.split(DELIMITER1).forEach {
                    if (it.isNotEmpty()) {
                        ClientSQLite(fromActivity).setOnlineProduct(it.split(DELIMITER2))
                    }
                }
            }
        }
    }

    fun setServerLists() {
        val myLists = arrayListOf<Lists>()
        val myProducts = arrayListOf<Product>()
        myLists.clear()
        myProducts.clear()
        ClientSQLite(fromActivity).getLists().forEach {
            if (!it.bOnline) {
                myLists.add(it)
            }
        }
        ClientSQLite(fromActivity).getProducts().forEach {
            if (!it.bOnline) {
                myProducts.add(it)
            }
        }
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", ClientSQLite(fromActivity).getUser().userName)
            .addFormDataPart("arrayOfLists",
                myLists.joinToString { it.sName + ";" + it.dCreated + ";" + it.dRealized + ";" + it.nPrice })
            .addFormDataPart(
                "arrayOfProducts",
                myProducts.joinToString { it.sName + ";" + it.nIdList }).build()
        MyRequest(fromActivity).phpQuery(INSERT_LIST_INTO_DB, requestBody1) { response ->
            val state = response.split(DELIMITER2)
            if (state.all { it == "OK" }) {
                MainController().showToast(fromActivity, R.string.accept)
                ClientSQLite(fromActivity).updateList(myLists, 1)
                ClientSQLite(fromActivity).updateProducts(myProducts, 1)
            } else {
//                RegisterActivity.getInstance().setError(fromActivity)
            }
        }
    }

    private fun setUser(user: UserFeatures, new: Boolean) {
        try {
            ClientSQLite(fromActivity).setUser(user)
            if (new) {
                RegisterActivity.getInstance().endRegister(fromActivity)
            } else {
                LoginActivity.getInstance().doAccess(fromActivity)
            }
        } catch (e: SQLException) {
            Log.e("USER", e.toString())
            MainController().showToast(fromActivity, R.string.error_at_insert_data)
        }
    }
}
