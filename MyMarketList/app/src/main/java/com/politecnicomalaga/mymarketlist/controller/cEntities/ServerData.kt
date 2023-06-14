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
import com.politecnicomalaga.mymarketlist.model.Catalogue
import com.politecnicomalaga.mymarketlist.model.List
import com.politecnicomalaga.mymarketlist.model.Product
import com.politecnicomalaga.mymarketlist.model.UserFeatures
import com.politecnicomalaga.mymarketlist.view.vActivities.CatalogueActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.EditActivity
import com.politecnicomalaga.mymarketlist.view.vActivities.ListActivity
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
    private val UPDATE_LIST_INTO_DB = "updateListIntoDB.php"

    fun getServerProductTables() {
        MyRequest(fromActivity).phpQuery(SELECT_FROM_TYPES) { response1 ->
            response1.split(DELIMITER1).forEach {
                if (it.isNotEmpty()) {
                    val myCatalogue = Catalogue()
                    myCatalogue.nId = it.split(DELIMITER2)[0].toInt()
                    myCatalogue.sType = it.split(DELIMITER2)[1]
                    CatalogueSQLite(fromActivity).setTypes(myCatalogue)
                }
            }
            MyRequest(fromActivity).phpQuery(SELECT_FROM_PRODUCTS) { response2 ->
                response2.split(DELIMITER1).forEach {
                    if (it.isNotEmpty()) {
                        val myProduct = Product()
                        myProduct.nId = it.split(DELIMITER2)[0].toInt()
                        myProduct.sName = it.split(DELIMITER2)[1]
                        myProduct.nIdType = it.split(DELIMITER2)[2].toInt()
                        CatalogueSQLite(fromActivity).setItems(myProduct)
                    }
                }
                if (response2.isEmpty()){
                    MainController().showToast(fromActivity, R.string.corrupt_data)
                }
            }
            if (response1.isEmpty()){
                MainController().showToast(fromActivity, R.string.corrupt_data)
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
                    )
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
                        setUser(user)
                    } else {
                        MainController().showToast(fromActivity, R.string.error_create_user)
                    }
                }
            }
            if (response1.isEmpty()){
                MainController().showToast(fromActivity, R.string.corrupt_data)
            }
        }
    }

    fun updateServerLists() {
        val myLists = arrayListOf<List>()
        myLists.clear()
        ClientSQLite(fromActivity).getLists().forEach {
            if (!it.bOnline && it.nPrice > 0F) {
                myLists.add(it)
            }
        }
        if (myLists.isNotEmpty()) {
            val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", ClientSQLite(fromActivity).getUser().userName)
                .addFormDataPart("arrayOfLists",
                    myLists.joinToString { it.sName + ";" + it.dCreated + ";" + it.dRealized + ";" + it.nPrice })
                .build()
            MyRequest(fromActivity).phpQuery(UPDATE_LIST_INTO_DB, requestBody) {
                if (it.isNotEmpty()) {
                    if (fromActivity is EditActivity) {
                        ClientSQLite(fromActivity).updateList(myLists, 1)
                        EditActivity.getInstance().endEdit(fromActivity)
                    } else {
                        MainController().showToast(fromActivity, R.string.in_maintenance)
                    }
                } else {
                    MainController().showToast(fromActivity, R.string.corrupt_data)
                }
            }
        } else {
            this@ServerData.setServerLists()
        }
    }

    private fun getServerLists() {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", ClientSQLite(fromActivity).getUser().userName).build()
        MyRequest(fromActivity).phpQuery(SELECT_LIST_FROM_DB, requestBody) { response1 ->
            ClientSQLite(fromActivity).resetTables(
                ClientSQLite(fromActivity).writableDatabase, true, false
            )
            response1.split(DELIMITER1).forEach {
                if (it.isNotEmpty()) {
                    val myList = List()
                    myList.nId = it.split(DELIMITER2)[0].toInt()
                    myList.sName = it.split(DELIMITER2)[1]
                    myList.dCreated = it.split(DELIMITER2)[2]
                    myList.dRealized = it.split(DELIMITER2)[3]
                    myList.nPrice = it.split(DELIMITER2)[4].toFloat()
                    ClientSQLite(fromActivity).setOnlineList(myList)
                }
            }
            MyRequest(fromActivity).phpQuery(SELECT_PRODUCTS_FROM_DB, requestBody) { response2 ->
                response2.split(DELIMITER1).forEach {
                    if (it.isNotEmpty()) {
                        val myProduct = Product()
                        myProduct.nId = it.split(DELIMITER2)[0].toInt()
                        myProduct.sName = it.split(DELIMITER2)[1]
                        myProduct.nIdList = it.split(DELIMITER2)[2].toInt()
                        ClientSQLite(fromActivity).setOnlineProduct(myProduct)
                    }
                }
                if (fromActivity is ListActivity) {
                    ListActivity.getInstance().endRefresh(fromActivity)
                }
                if (response2.isEmpty()){
                    MainController().showToast(fromActivity, R.string.corrupt_data)
                }
            }
            if (response1.isEmpty()){
                MainController().showToast(fromActivity, R.string.corrupt_data)
            }
        }
    }

    private fun setServerLists() {
        val myLists = arrayListOf<List>()
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
        if (myLists.isNotEmpty() && myProducts.isNotEmpty()) {
            val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", ClientSQLite(fromActivity).getUser().userName)
                .addFormDataPart("arrayOfLists",
                    myLists.joinToString { it.sName + ";" + it.dCreated + ";" + it.dRealized + ";" + it.nPrice })
                .addFormDataPart(
                    "arrayOfProducts",
                    myProducts.joinToString { it.sName + ";" + it.nIdList }).build()
            MyRequest(fromActivity).phpQuery(INSERT_LIST_INTO_DB, requestBody) { response ->
                val state = response.split(DELIMITER2)
                if (state.all { it == "OK" }) {
                    if (fromActivity is CatalogueActivity) {
                        ClientSQLite(fromActivity).updateList(myLists, 1)
                        ClientSQLite(fromActivity).updateProducts(myProducts, 1)
                        CatalogueActivity.getInstance().endCatalogue(fromActivity)
                        this@ServerData.getServerLists()
                    } else {
                        MainController().showToast(fromActivity, R.string.in_maintenance)
                    }
                } else {
                    MainController().showToast(fromActivity, R.string.corrupt_data)
                }
            }
        } else {
            this@ServerData.getServerLists()
        }
    }

    private fun setUser(user: UserFeatures) {
        try {
            ClientSQLite(fromActivity).setUser(user)
            when (fromActivity) {
                is RegisterActivity -> {
                    RegisterActivity.getInstance().endRegister(fromActivity)
                }

                is LoginActivity -> {
                    LoginActivity.getInstance().doAccess(fromActivity)
                }

                else -> MainController().showToast(fromActivity, R.string.in_maintenance)
            }
        } catch (e: SQLException) {
            Log.e("USER", e.toString())
            MainController().showToast(fromActivity, R.string.corrupt_data)
        }
    }
}
