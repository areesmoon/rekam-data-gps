package net.senosoft.rekamdatagps.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "RekamData.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE data_lapangan (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT,
                deskripsi TEXT,
                alamat TEXT,
                latitude REAL,
                longitude REAL,
                foto BLOB
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS data_lapangan")
        onCreate(db)
    }

    fun insertData(data: DataModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nama", data.nama)
            put("deskripsi", data.deskripsi)
            put("alamat", data.alamat)
            put("latitude", data.latitude)
            put("longitude", data.longitude)
            put("foto", data.foto)
        }
        val result = db.insert("data_lapangan", null, values)
        return result != -1L
    }

    fun getAllData(): List<DataModel> {
        val dataList = mutableListOf<DataModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM data_lapangan", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"))
                val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"))
                val alamat = cursor.getString(cursor.getColumnIndexOrThrow("alamat"))
                val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
                val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
                val foto = if (!cursor.isNull(cursor.getColumnIndexOrThrow("foto"))) {
                    cursor.getBlob(cursor.getColumnIndexOrThrow("foto"))
                } else null

                val model = DataModel(id, nama, deskripsi, alamat, latitude, longitude, foto)
                dataList.add(model)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return dataList
    }

}
