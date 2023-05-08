import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MarketListSQLite private constructor(fromContext: Context) :
    SQLiteOpenHelper(fromContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private var myMarketListSQLite: MarketListSQLite? = null
        fun getInstance(fromContext: Context): MarketListSQLite {
            if (myMarketListSQLite == null) {
                myMarketListSQLite = MarketListSQLite(fromContext)
            }
            return myMarketListSQLite!!
        }

        const val DATABASE_NAME = "DBProducts.db"
        const val DATABASE_VERSION = 1
        lateinit var db: SQLiteDatabase

        // Table xxx
        val PRODUCT_ID = arrayOf("id", "0")
        val PRODUCT_ES = arrayOf("es", "1")
//        val PRODUCT_ID = arrayOf("es", "0")


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

    fun getTables(): Cursor {
        return db.query(
            "sqlite_master", arrayOf("name"), "type = ?", arrayOf("table"), null, null, "name"
        )
    }

    fun createTable(table: String) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $table (id INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCT_ES[0] + " TEXT PRIMARY KEY NOT NULL)"
        )
    }

    fun deleteTable(table: String) {
        db.execSQL(
            "DROP TABLE IF EXISTS $table"
        )
    }

    fun getProducts(table: String): Cursor {
        return db.query(
            table, null, null, null, null, null, PRODUCT_ES[0]
        )
    }

    fun insertProduct(table: String, product: String) {
        db.execSQL("INSERT OR REPLACE INTO $table VALUES (null, '$product')")
    }

    fun deleteProduct(table: String, product: String) {
        db.delete(table, PRODUCT_ES[0] + " = ?", arrayOf(product))
    }

    fun getDb(): SQLiteDatabase {
        return db
    }


}