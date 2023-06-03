package com.politecnicomalaga.mymarketlist.controller.cEntities

import android.app.Activity
import android.database.SQLException
import android.util.Base64
import com.politecnicomalaga.mymarketlist.R
import com.politecnicomalaga.mymarketlist.controller.MainController
import com.politecnicomalaga.mymarketlist.controller.cHTTP.MyRequest
import com.politecnicomalaga.mymarketlist.controller.cSQLite.CatalogueSQLite
import com.politecnicomalaga.mymarketlist.controller.cSQLite.ClientSQLite
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
    private val INSERT_LIST = "insertList.php"

    fun getServerProductTables() {
        MyRequest(fromActivity).phpQuery(SELECT_FROM_TYPES) {
            setTypesTable(it.split(DELIMITER1))
        }
        MyRequest(fromActivity).phpQuery(SELECT_FROM_PRODUCTS) {
            setProductsTable(it.split(DELIMITER1))
        }
    }

    fun getServerUser(username: String, pass: String) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username).addFormDataPart("pass", pass).build()
        MyRequest(fromActivity).phpQuery(SELECT_FROM_DATABASE, requestBody) { userResponse ->
            if (userResponse.isNotEmpty()) {
                MyRequest(fromActivity).phpQueryImage(
                    SELECT_FROM_DATABASE_IMAGE, requestBody
                ) { imageResponse ->
                    if (imageResponse.isNotEmpty()) {
                        setUser(
                            UserFeatures(
                                userResponse.split(DELIMITER2)[0],
                                userResponse.split(DELIMITER2)[1],
                                userResponse.split(DELIMITER2)[2],
                                imageResponse
                            ), new = false
                        )
                    } else {
                        LoginActivity.getInstance()
                            .setError(fromActivity, R.string.error_get_user_data)
                    }
                }
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
                        setUser(user, new = true)
                    } else {
                        RegisterActivity.getInstance().setError(fromActivity)
                    }
                }
            }
        }
    }

    fun getServerLists() {

    }

    fun setServerLists() {
        val myLists = ClientSQLite(fromActivity).getLists()
        val myProducts = arrayListOf(Product())
        myProducts.clear()
        myLists.forEach { l ->
            if (l.bOnline) {
                myLists.remove(l)
            } else {
                myProducts.addAll(ClientSQLite(fromActivity).getProductsList(l))
            }
            // Hacer update online true a la lista l
        }
        val requestBody1 = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", ClientSQLite(fromActivity).getUser().userName)
            .addFormDataPart("arrayOfLists",
                myLists.joinToString {it.sName + ";" + it.dCreated + ";" + it.dRealized + ";" + it.nPrice})
            .addFormDataPart("arrayOfProducts",
                myProducts.joinToString {it.sName + ";" + it.nIdList}).build()
        MyRequest(fromActivity).phpQuery(INSERT_LIST, requestBody1) { response ->
            val state = response.split(DELIMITER2)
            if (state.all { it == "OK" }) {
                MainController().showToast(fromActivity, R.string.accept)
                ClientSQLite(fromActivity).updateList(myLists)
            } else {
//                RegisterActivity.getInstance().setError(fromActivity)
            }
        }
    }

    private fun setTypesTable(table: List<String>) {
        table.forEach {
            if (it.isNotEmpty()) {
                CatalogueSQLite(fromActivity).setTypes(it.split(DELIMITER2))
            }
        }
    }

    private fun setProductsTable(products: List<String>) {
        products.forEach {
            if (it.isNotEmpty()) {
                CatalogueSQLite(fromActivity).setItems(it.split(DELIMITER2))
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
            MainController().showToast(fromActivity, R.string.error_at_insert_data)
        }
    }
}
