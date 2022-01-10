package com.example.notebookactivity.db


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.getStringOrNull



class MyDbManager(context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null
    val s = "mdbm"

    // Открыть БД
    fun openDb(){
        db = myDbHelper.writableDatabase
        Log.e(s,"открыть")
    }


    // Записать данные в БД
    fun insertToDb(title: String, content: String, uri: String){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
        Log.e(s,"запись")
    }

    // Считать данные с БД
    fun readDbData(): ArrayList<String>{
        val dataList = ArrayList<String>()

        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null,null,
            null,null,null,)


        while(cursor?.moveToNext()!!){
            val dataText = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
            dataList.add(dataText.toString())
        }
        cursor.close()
        Log.e(s,"считать")
        return dataList
    }

    // Закрыть БД
    fun closeDb(){
        myDbHelper.close()
    }

}