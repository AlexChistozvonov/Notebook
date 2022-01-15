package com.example.notebookactivity.db


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.example.notebookactivity.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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
    suspend fun insertToDb(title: String, content: String, uri: String, time: String) = withContext(Dispatchers.IO){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri)
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
        Log.e(s,"запись")
    }

    // Удаление из БД
    fun removeItemFromDb( id: String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }


    suspend fun ubdateItem(title: String, content: String, uri: String, id: Int, time: String) = withContext(Dispatchers.IO){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri)
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)
        Log.e(s,"запись")
    }


    // Считать данные с БД
    suspend fun readDbData(searchText: String): ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like?"
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, selection, arrayOf("%$searchText%"),
            null,null,null,)


        while(cursor?.moveToNext()!!){
            val dataTitle = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
            val dataUri = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
            val dataId = cursor.getIntOrNull(cursor.getColumnIndex(BaseColumns._ID))
            val dataTime = cursor.getStringOrNull(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TIME))
            val item = ListItem()
            item.title = dataTitle.toString()
            item.desc = dataContent.toString()
            item.uri = dataUri.toString()
            item.id = dataId!!
            item.time = dataTime!!
            dataList.add(item)
        }
        cursor.close()
        Log.e(s,"считать")
        return@withContext dataList
    }

    // Закрыть БД
    fun closeDb(){
        myDbHelper.close()
    }

}