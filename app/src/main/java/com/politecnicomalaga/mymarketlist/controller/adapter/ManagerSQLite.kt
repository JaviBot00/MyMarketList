package com.politecnicomalaga.mymarketlist.controller.adapter

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ManagerSQLite(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        const val DATABASE_NAME = "DBProducts.db"
        const val DATABASE_VERSION = 1
        lateinit var myContext: Context
        lateinit var db: SQLiteDatabase

        const val DB_TUsers = "TUsers"
        val TUsers_USER = arrayOf("cUser", "0")
        val TUsers_PASSWORD = arrayOf("cPassword", "1")
        val TUsers_BIRTHDATE = arrayOf("dBirthdate", "2")
        val TUsers_IMGPROFILE = arrayOf("bImgProfile", "3")
        val TUsers_ROLE = arrayOf("cRole", "4")

        const val DB_TUsersGroups = "TUsersGroups"
        val TUsersGroups_ROLE = arrayOf("cRole", "0")
        val TUG_ROLES = arrayOf("Admin", "Estandar", "Invitado")
    }

    init {
        myContext = context
    }

    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL(
//            "CREATE TABLE " + DB_TUsers + " ( " + TUsers_USER[0] + " VARCHAR(30) PRIMARY KEY," + TUsers_PASSWORD[0] + " VARCHAR(30) NOT NULL, " + TUsers_BIRTHDATE[0] + " VARCHAR(10) NOT NULL, " + TUsers_IMGPROFILE[0] + " BLOB, " + TUsers_ROLE[0] + " VRCHAR(10) NOT NULL) "
//        )
//
//        db.execSQL(
//            "CREATE TABLE " + DB_TUsersGroups + " ( " + TUsersGroups_ROLE[0] + " VARCHAR(10) PRIMARY KEY)"
//        )
//
//        for (st in TUG_ROLES) {
//            db.execSQL("INSERT INTO " + DB_TUsersGroups + " ( " + TUsersGroups_ROLE[0] + " ) VALUES ( '" + st + "' ) ")
////            db.execSQL("INSERT INTO " + DB_TUsersGroups + " VALUES ( '" + st + "' ) ");
//        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun setReadable() {
        db = this.readableDatabase
    }

    fun setWritable() {
        db = this.writableDatabase
    }

    fun createTable(table: String) {
        db.execSQL(
            "CREATE TABLE $table ( nombre TEXT PRIMARY KEY)"
        )
    }

    fun getUserLogin(user: String, password: String): Cursor {
        return db.query(
            DB_TUsers,
            null,
            TUsers_USER[0] + " = ? AND " + TUsers_PASSWORD[0] + " = ?",
            arrayOf(user, password),
            null,
            null,
            null
        )
    }

    fun getOneUser(user: String): Cursor {
        return db.query(DB_TUsers, null, TUsers_USER[0] + " = ?", arrayOf(user), null, null, null)
    }

    fun getUsers(): Cursor {
        return db.query(DB_TUsers, null, null, null, null, null, TUsers_USER[0])
    }

    fun getGroups(): Cursor {
        return db.query(DB_TUsersGroups, null, null, null, null, null, TUsersGroups_ROLE[0])
    }

    fun insertUser(values: ContentValues) {
        db.insert(DB_TUsers, null, values)
    }

    fun updateUser(values: ContentValues, user: String) {
        db.update(
            DB_TUsers, values, TUsers_USER[0] + " = ?", arrayOf(user)
        )
    }

    fun deleteUser(user: String) {
        db.delete(
            DB_TUsers, TUsers_USER[0] + " = ?", arrayOf(user)
        )
    }

    fun getDb(): SQLiteDatabase {
        return db
    }

}