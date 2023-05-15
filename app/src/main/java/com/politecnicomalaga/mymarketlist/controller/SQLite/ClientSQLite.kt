package com.politecnicomalaga.mymarketlist.controller.SQLite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClientSQLite(fromContext: Context) :
    SQLiteOpenHelper(fromContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        const val DATABASE_NAME = "DBClient.db"
        const val DATABASE_VERSION = 1
        lateinit var db: SQLiteDatabase

        const val DB_TUsers = "TUsers"
        val TUsers_USER = arrayOf("cUser", "0")
        val TUsers_PASSWORD = arrayOf("cPassword", "1")
        val TUsers_EMAIL = arrayOf("cEmail", "2")
        val TUsers_IMGPROFILE = arrayOf("bImgProfile", "3")

        const val DB_TList = "TList"
        val TList_ID = arrayOf("nID", "0")
        val TList_NAME = arrayOf("name", "1")
        val TList_DCREATED = arrayOf("dCreated", "2")
        val TList_DREALIZED = arrayOf("dRealized", "3")
        val TList_Price = arrayOf("nPrice", "4")
        val TList_OnLine = arrayOf("bOnLine", "5")

        // Table xxx
        val TProduct_NAME = arrayOf("name", "1")
        val TProduct_AMOUNT = arrayOf("amount", "2")
//        val PRODUCT_ID = arrayOf("en", "2")
    }

//    override fun onConfigure(db: SQLiteDatabase) {
//        super.onConfigure(db)
//        db.execSQL("PRAGMA foreign_keys = ON")
//    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $DB_TUsers ( " + TUsers_USER[0] + " TEXT PRIMARY KEY, " + TUsers_PASSWORD[0] + " TEXT NOT NULL, " + TUsers_EMAIL[0] + " TEXT, " + TUsers_IMGPROFILE[0] + " BLOB) "
        )

        db.execSQL(
            "CREATE TABLE $DB_TList ( " + TList_ID[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TList_NAME[0] + " TEXT NOT NULL, " + TList_DCREATED[0] + " DATETIME NOT NULL, " + TList_DREALIZED[0] + " DATETIME, " + TList_Price[0] + " REAL, " + TList_OnLine[0] + " BOOLEAN NOT NULL) "
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun setWritable() {
        db = this.writableDatabase
    }

    fun setReadable() {
        db = this.readableDatabase
    }

    fun getLists(): Cursor {
        return db.query(
            DB_TList, null, null, null, null, null, TList_DCREATED[0]
        )
    }

    fun insertList(name: String) {
        db.execSQL("INSERT OR REPLACE INTO $DB_TList VALUES(null, '$name', datetime('now'), null, null, false)")
    }

    fun createTable(table: String) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $table ( " + TProduct_NAME[0] + " TEXT PRIMARY KEY)"
        )
    }

//    fun deleteTable(table: String) {
//        db.execSQL(
//            "DROP TABLE IF EXISTS $table"
//        )
//    }

    fun getProducts(table: String): Cursor {
        return db.query(
            table, null, null, null, null, null, TProduct_NAME[0]
        )
    }

    fun insertProduct(table: String, product: String) {
        db.execSQL("INSERT OR REPLACE INTO $table VALUES ('$product')")
    }

//    fun deleteProduct(table: String, product: String) {
//        db.delete(table, TProduct_NAME[0] + " = ?", arrayOf(product))
//    }

    fun getDb(): SQLiteDatabase {
        return db
    }

}