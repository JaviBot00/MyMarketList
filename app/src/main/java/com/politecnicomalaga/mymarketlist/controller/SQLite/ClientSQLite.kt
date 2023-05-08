import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClientSQLite private constructor(fromContext: Context) :
    SQLiteOpenHelper(fromContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private var myClientSQLite: ClientSQLite? = null
        fun getInstance(fromContext: Context): ClientSQLite {
            if (myClientSQLite == null) {
                myClientSQLite = ClientSQLite(fromContext)
            }
            return myClientSQLite!!
        }

        const val DATABASE_NAME = "DBClient.db"
        const val DATABASE_VERSION = 1
        lateinit var db: SQLiteDatabase

        const val DB_TUsers = "TUsers"
        val TUsers_USER = arrayOf("cUser", "0")
        val TUsers_PASSWORD = arrayOf("cPassword", "1")
        val TUsers_BIRTHDATE = arrayOf("dBirthdate", "2")
        val TUsers_IMGPROFILE = arrayOf("bImgProfile", "3")

        const val DB_TUsersGroups = "TList"
        val TUsersGroups_ROLE = arrayOf("cRole", "0")
        val TUG_ROLES = arrayOf("Admin", "Estandar", "Invitado")
    }


    val TPRODUCTS = arrayOf("es", "0")


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
            "CREATE TABLE IF NOT EXISTS $table (" + TPRODUCTS[0] + " TEXT PRIMARY KEY)"
        )
    }

    fun deleteTable(table: String) {
        db.execSQL(
            "DROP TABLE IF EXISTS $table"
        )
    }

    fun getProducts(table: String): Cursor {
        return db.query(
            table, null, null, null, null, null, TPRODUCTS[0]
        )
    }

    fun insertProduct(table: String, product: String) {
        db.execSQL("INSERT OR REPLACE INTO $table VALUES ('$product')")
    }

    fun deleteProduct(table: String, product: String) {
        db.delete(table, TPRODUCTS[0] + " = ?", arrayOf(product))
    }

    fun getDb(): SQLiteDatabase {
        return db
    }


}