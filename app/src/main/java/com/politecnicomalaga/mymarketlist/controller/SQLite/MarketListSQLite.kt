import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MarketListSQLite(fromContext: Context) :
    SQLiteOpenHelper(fromContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "DBProducts.db"
        const val DATABASE_VERSION = 1
        lateinit var db: SQLiteDatabase

        const val DB_TCategories = "TCategories"
        val TCategories_ID = arrayOf("nID", "0")
        val TCategories_TYPE = arrayOf("cTypes", "1")

        // Table xxx
        val TProduct_ID = arrayOf("id", "0")
        val TProduct_ES = arrayOf("es", "1")
//        val PRODUCT_ID = arrayOf("en", "2")


    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $DB_TCategories ('" + TCategories_ID[0] + "' INTEGER PRIMARY KEY NOT NULL, '" + TCategories_TYPE[0] + "' TEXT NOT NULL)"
        )
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

    fun setWritable() {
        db = this.writableDatabase
    }

    fun setReadable() {
        db = this.readableDatabase
    }

    fun createTable(table: String) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $table ('" + TProduct_ID[0] + "' INTEGER PRIMARY KEY NOT NULL, '" + TProduct_ES[0] + "' TEXT NOT NULL)"
        )
    }

    fun getTables(): Cursor {
        return db.query(
            DB_TCategories,
            null,
            null,
            null,
            null,
            null,
            TCategories_TYPE[0]
        )
    }

    fun insertTables(id: String, table: String) {
        db.execSQL("INSERT OR REPLACE INTO $DB_TCategories VALUES ($id, '$table')")

    }

//    fun deleteTable(table: String) {
//        db.execSQL(
//            "DROP TABLE IF EXISTS $table"
//        )
//    }

    fun getProducts(table: String): Cursor {
        return db.query(
            table, null, null, null, null, null, TProduct_ES[0]
        )
    }

    fun insertProduct(table: String, id: String, product: String) {
        db.execSQL("INSERT OR REPLACE INTO $table VALUES ($id, '$product')")
    }

//    fun deleteProduct(table: String, product: String) {
//        db.delete(table, TProduct_ES[0] + " = ?", arrayOf(product))
//    }

    fun getDb(): SQLiteDatabase {
        return db
    }


}