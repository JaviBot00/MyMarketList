package com.politecnicomalaga.mymarketlist.controller.cSQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.politecnicomalaga.mymarketlist.model.Catalogue
import com.politecnicomalaga.mymarketlist.model.Product

class CatalogueSQLite(fromContext: Context) :
    SQLiteOpenHelper(fromContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "DBProducts.db"
        const val DATABASE_VERSION = 3
    }

    private val DB_TYPES = "Types"
    private val tTYPES_ID = arrayOf("nId", "0")
    private val tTYPES_NAME = arrayOf("sName", "1")

    private val DB_PRODUCTS = "Products"
    private val tPRODUCTS_ID = arrayOf("nId", "0")
    private val tPRODUCTS_NAME = arrayOf("sName", "1")
    private val tPRODUCTS_IdType = arrayOf("nIdType", "2")

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        resetTables(db, false)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        resetTables(db, true)
    }

    override fun close() {
        super.close()
        this.writableDatabase.close()
        this.readableDatabase.close()
    }

    fun getCatalogue(): ArrayList<Catalogue> {
        val myCatalogue = arrayListOf(Catalogue())
        myCatalogue.clear()
        val typeCursor: Cursor =
            this.readableDatabase.query(DB_TYPES, null, null, null, null, null, tTYPES_NAME[0])

        typeCursor.use { tc ->
            while (tc.moveToNext()) {
                val type = Catalogue()
                type.nId = tc.getInt(tTYPES_ID[1].toInt())
                type.sType = tc.getString(tTYPES_NAME[1].toInt())
                type.oProducts = getItems(type.nId)
                myCatalogue.add(type)
            }
        }
        return myCatalogue

    }

    fun setTypes(catalogueData: Catalogue) {
        val typeValues = ContentValues()
        typeValues.put(tTYPES_ID[0], catalogueData.nId)
        typeValues.put(tTYPES_NAME[0], catalogueData.sType)
        this.writableDatabase.insertWithOnConflict(
            DB_TYPES, null, typeValues, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    private fun getItems(type: Int): ArrayList<Product> {
        val myItems = arrayListOf(Product())
        myItems.clear()
        val itemCursor: Cursor = this.readableDatabase.query(
            DB_PRODUCTS,
            null,
            tPRODUCTS_IdType[0] + " = ?",
            arrayOf(type.toString()),
            null,
            null,
            tPRODUCTS_NAME[0]
        )
        itemCursor.use { ic ->
            while (ic.moveToNext()) {
                val item = Product()
                item.nId = ic.getInt(tPRODUCTS_ID[1].toInt())
                item.sName = ic.getString(tPRODUCTS_NAME[1].toInt())
                item.nIdType = ic.getInt(tPRODUCTS_IdType[1].toInt())
                myItems.add(item)
            }
        }
        return myItems
    }

    fun setItems(productData: Product) {
        val productValues = ContentValues()
        productValues.put(tPRODUCTS_ID[0], productData.nId)
        productValues.put(tPRODUCTS_NAME[0], productData.sName)
        productValues.put(tPRODUCTS_IdType[0], productData.nIdType)
        this.writableDatabase.insertWithOnConflict(
            DB_PRODUCTS, null, productValues, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    private fun resetTables(db: SQLiteDatabase, reset: Boolean) {
        if (reset) {
            db.execSQL("DROP TABLE IF EXISTS $DB_TYPES")
            db.execSQL("DROP TABLE IF EXISTS $DB_PRODUCTS")
        }

        db.execSQL(
            "CREATE TABLE $DB_TYPES ('" + tTYPES_ID[0] + "' INTEGER PRIMARY KEY NOT NULL, '" + tTYPES_NAME[0] + "' TEXT NOT NULL)"
        )

        db.execSQL(
            "CREATE TABLE $DB_PRODUCTS  ( " + tPRODUCTS_ID[0] + " INTEGER PRIMARY KEY NOT NULL, '" + tPRODUCTS_NAME[0] + "' TEXT NOT NULL, '" + tPRODUCTS_IdType[0] + "' INTEGER NOT NULL, FOREIGN KEY ('" + tPRODUCTS_IdType[0] + "') REFERENCES $DB_TYPES ('" + tTYPES_ID[0] + "'))"
        )
    }

}