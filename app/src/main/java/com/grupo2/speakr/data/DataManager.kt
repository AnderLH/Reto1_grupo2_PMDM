import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.grupo2.speakr.data.LoginUser

class DataManager(context: Context) {

    companion object {
        private const val DATABASE_NAME = "LogDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Logs"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        private const val TABLE_CREATE =
            "CREATE TABLE $TABLE_NAME ($COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT);"
    }

    private var context: Context = context
    private var database: SQLiteDatabase? = null
    private var dbHelper: DatabaseHelper? = null

    fun open(): DataManager {
        dbHelper = DatabaseHelper(context)
        database = dbHelper?.writableDatabase
        return this
    }

    fun close() {
        dbHelper?.close()
    }

    fun insertLog(email: String, password: String): Long {
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        return database?.insert(TABLE_NAME, null, values) ?: -1
    }

    fun getAllLogs(): Cursor? {
        return database?.query(TABLE_NAME, arrayOf(COLUMN_EMAIL, COLUMN_PASSWORD), null, null, null, null, null)
    }

    fun deleteAllLogs() {
        database?.delete(TABLE_NAME, null, null)
    }


    private inner class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(TABLE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }
    fun getLastLog(): LoginUser? {
        val cursor = database?.query(
            TABLE_NAME,
            arrayOf(COLUMN_EMAIL, COLUMN_PASSWORD),
            null, null, null, null,
            "ROWID DESC",  // Order by ROWID in descending order to get the last added log
            "1"  // Limit the result to 1 row
        )

        var logEntry: LoginUser? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val emailIndex = it.getColumnIndex(COLUMN_EMAIL)
                val passwordIndex = it.getColumnIndex(COLUMN_PASSWORD)
                val email = it.getString(emailIndex)
                val password = it.getString(passwordIndex)
                logEntry = LoginUser(email, password)
            }
        }

        return logEntry
    }
    fun deleteAllRows() {
        database?.delete(TABLE_NAME, null, null)
    }

}
