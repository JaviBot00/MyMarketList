package com.politecnicomalaga.mymarketlist.controller.cSQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Editable
import com.politecnicomalaga.mymarketlist.model.Lists
import com.politecnicomalaga.mymarketlist.model.Product
import com.politecnicomalaga.mymarketlist.model.UserFeatures
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClientSQLite(fromContext: Context) :
    SQLiteOpenHelper(fromContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val myProductsList = sortedSetOf<Product>()

        const val DATABASE_NAME = "DBClient.db"
        const val DATABASE_VERSION = 2
    }

    private val DB_USER = "User"
    private val tUSER_UserName = arrayOf("cUserName", "0")
    private val tUser_PassWord = arrayOf("cPassWord", "1")
    private val tUser_EMAIL = arrayOf("cEmail", "2")
    private val tUser_ImgProfile = arrayOf("bImgProfile", "3")

    private val DB_LIST = "List"
    private val tLIST_ID = arrayOf("nId", "0")
    private val tLIST_NAME = arrayOf("sName", "1")
    private val tLIST_CREATED = arrayOf("dCreated", "2")
    private val tLIST_REALIZED = arrayOf("dRealized", "3")
    private val tLIST_PRICE = arrayOf("nPrice", "4")
    private val tLIST_OnLine = arrayOf("bOnLine", "5")

    private val DB_PRODUCTS = "Products"
    private val tPRODUCT_ID = arrayOf("nId", "0")
    private val tPRODUCT_NAME = arrayOf("sName", "1")
    private val tPRODUCT_IdList = arrayOf("nIdList", "2")
    private val tPRODUCT_OnLine = arrayOf("bOnLine", "3")

//    override fun onConfigure(db: SQLiteDatabase) {
//        super.onConfigure(db)
//        db.execSQL("PRAGMA foreign_keys = ON")
//    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        resetTables(db, false, false)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        resetTables(db, true, false)
    }

    override fun close() {
        super.close()
        this.writableDatabase.close()
        this.readableDatabase.close()
    }

    fun getUser(): UserFeatures {
        val user = UserFeatures()
        val userCursor: Cursor =
            this.readableDatabase.query(DB_USER, null, null, null, null, null, null)
        userCursor.use {
            if (it.moveToNext()) {
                user.userName = it.getString(Integer.parseInt(tUSER_UserName[1]))
                user.passWord = it.getString(Integer.parseInt(tUser_PassWord[1]))
                user.email = it.getString(Integer.parseInt(tUser_EMAIL[1]))
                user.imgProfileWeb = it.getString(Integer.parseInt(tUser_ImgProfile[1]))
            }
        }
        return user
    }

    fun setUser(user: UserFeatures) {
        val userValues = ContentValues()
        userValues.put(tUSER_UserName[0], user.userName)
        userValues.put(tUser_PassWord[0], user.passWord)
        userValues.put(tUser_EMAIL[0], user.email)
        userValues.put(tUser_ImgProfile[0], user.imgProfileWeb)
        this.writableDatabase.insertWithOnConflict(
            DB_USER, null, userValues, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun getLists(): ArrayList<Lists> {
        val lists = arrayListOf(Lists())
        lists.clear()
        val listCursor: Cursor = this.readableDatabase.query(
            DB_LIST, null, null, null, null, null, tLIST_CREATED[0]
        )
        listCursor.use {
            while (it.moveToNext()) {
                val list = Lists()
                list.nId = it.getInt(Integer.parseInt(tLIST_ID[1]))
                list.sName = it.getString(Integer.parseInt(tLIST_NAME[1]))
                list.dCreated = it.getString(Integer.parseInt(tLIST_CREATED[1]))
                list.dRealized = it.getString(Integer.parseInt(tLIST_REALIZED[1]))
                list.nPrice = it.getFloat(Integer.parseInt(tLIST_PRICE[1]))
                list.bOnline = (it.getInt(Integer.parseInt(tLIST_OnLine[1])) == 1)
                lists.add(list)
            }
        }
        return lists
    }

    fun setList(text: Editable) {
        val txtReplace: String = text.toString().replace(" ", "_", false)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es", "ES"))

        val listValues = ContentValues()
        listValues.put(tLIST_NAME[0], txtReplace)
        listValues.put(tLIST_CREATED[0], simpleDateFormat.format(Date()))
        listValues.put(tLIST_OnLine[0], 0)
        this.writableDatabase.insert(DB_LIST, null, listValues)
        val idList = getLists()[getLists().lastIndex].nId
        myProductsList.forEach {
            setProduct(it.sName, idList)
        }
        myProductsList.clear()
    }

    fun updateList(myLists: ArrayList<Lists>, online: Int) {
        myLists.forEach {
            val values = ContentValues()
            values.put(tLIST_OnLine[0], online)
            this.writableDatabase.update(
                DB_LIST, values, tLIST_ID[0] + " = ?", arrayOf(it.nId.toString())
            )
        }
    }

    fun getProducts(): ArrayList<Product> {
        val myProducts = arrayListOf(Product())
        myProducts.clear()
        val productCursor: Cursor = this.readableDatabase.query(
            DB_PRODUCTS, null, null, null, null, null, tPRODUCT_NAME[0]
        )
        productCursor.use {
            while (productCursor.moveToNext()) {
                val product = Product()
                product.nId = it.getInt(Integer.parseInt(tPRODUCT_ID[1]))
                product.sName = it.getString(Integer.parseInt(tPRODUCT_NAME[1]))
                product.nIdList = it.getInt(Integer.parseInt(tPRODUCT_IdList[1]))
                product.bOnline = (it.getInt(Integer.parseInt(tPRODUCT_OnLine[1])) == 1)
                myProducts.add(product)
            }
        }
        return myProducts
    }

    fun updateProducts(myProducts: ArrayList<Product>, online: Int) {
        myProducts.forEach {
            val values = ContentValues()
            values.put(tPRODUCT_OnLine[0], online)
            this.writableDatabase.update(
                DB_PRODUCTS, values, tPRODUCT_ID[0] + " = ?", arrayOf(it.nId.toString())
            )
        }
    }

    fun setOnlineList(dataList: List<String>) {
        val listValues = ContentValues()
        listValues.put(tLIST_ID[0], dataList[0])
        listValues.put(tLIST_NAME[0], dataList[1])
        listValues.put(tLIST_CREATED[0], dataList[2])
        listValues.put(tLIST_REALIZED[0], dataList[3])
        listValues.put(tLIST_PRICE[0], dataList[4])
        listValues.put(tLIST_OnLine[0], 1)
        this.writableDatabase.insertWithOnConflict(
            DB_LIST, null, listValues, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun setOnlineProduct(dataProduct: List<String>) {
        val productValues = ContentValues()
        productValues.put(tPRODUCT_ID[0], dataProduct[0])
        productValues.put(tPRODUCT_NAME[0], dataProduct[1])
        productValues.put(tPRODUCT_IdList[0], dataProduct[2])
        productValues.put(tPRODUCT_OnLine[0], 1)
        this.writableDatabase.insertWithOnConflict(
            DB_PRODUCTS, null, productValues, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    private fun setProduct(name: String, idList: Int) {
        val productsValues = ContentValues()
        productsValues.put(tPRODUCT_NAME[0], name)
        productsValues.put(tPRODUCT_IdList[0], idList)
        productsValues.put(tPRODUCT_OnLine[0], 0)
        this.writableDatabase.insert(DB_PRODUCTS, null, productsValues)
    }


    fun resetTables(db: SQLiteDatabase, reset: Boolean, user: Boolean) {
        if (user) {
            db.execSQL("DROP TABLE IF EXISTS $DB_USER")

            db.execSQL(
                "CREATE TABLE $DB_USER ( " + tUSER_UserName[0] + " TEXT PRIMARY KEY, " + tUser_PassWord[0] + " TEXT NOT NULL, " + tUser_EMAIL[0] + " TEXT NOT NULL, " + tUser_ImgProfile[0] + " TEXT NOT NULL) "
            )
        }
        if (reset) {
            db.execSQL("DROP TABLE IF EXISTS $DB_LIST")
            db.execSQL("DROP TABLE IF EXISTS $DB_PRODUCTS")

            db.execSQL(
                "CREATE TABLE $DB_LIST ( " + tLIST_ID[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tLIST_NAME[0] + " TEXT NOT NULL, " + tLIST_CREATED[0] + " DATETIME NOT NULL, " + tLIST_REALIZED[0] + " DATETIME, " + tLIST_PRICE[0] + " REAL, " + tLIST_OnLine[0] + " BOOLEAN NOT NULL) "
            )

            db.execSQL(
                "CREATE TABLE $DB_PRODUCTS ( " + tPRODUCT_ID[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tPRODUCT_NAME[0] + " TEXT NOT NULL, " + tPRODUCT_IdList[0] + " INTEGER NOT NULL , " + tPRODUCT_OnLine[0] + " BOOLEAN NOT NULL)"
            )
        }
    }

}