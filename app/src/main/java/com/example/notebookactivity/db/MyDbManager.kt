package com.example.notebookactivity.db


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.example.notebookactivity.ListItem


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

    fun removeItemFromDb( id: String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }



    // Считать данные с БД
    fun readDbData(): ArrayList<ListItem>{
        val dataList = ArrayList<ListItem>()

        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null,null,
            null,null,null,)


        while(cursor?.moveToNext()!!){
            val dataTitle = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
            val dataUri = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
            val dataId = cursor.getIntOrNull(cursor.getColumnIndex(BaseColumns._ID))
            val item = ListItem()
            item.title = dataTitle.toString()
            item.desc = dataContent.toString()
            item.uri = dataUri.toString()
            item.id = dataId!!
            dataList.add(item)
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