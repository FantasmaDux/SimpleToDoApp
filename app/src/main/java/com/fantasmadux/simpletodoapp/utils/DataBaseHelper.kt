package com.fantasmadux.simpletodoapp.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.fantasmadux.simpletodoapp.model.ToDoModel
import java.util.logging.Logger

// save all the date
class DataBaseHelper(
    context: Context?,
    private val DATABASE_NAME: String = "TODO_DATABASE",
    private val DATABASE_VERSION: Int = 1,
    private val TABLE_NAME: String = "TODO_TABLE",
    private val COL_1: String = "ID",
    private val COL_2: String = "TASK",
    private val COL_3: String = "STATUS",
    private val COL_4: String = "DATE",
    private val COL_5: String = "TIME",

    ) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val log = Logger.getLogger(DataBaseHelper::class.java.name)


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TASK TEXT, " +
                    "STATUS INTEGER, " +
                    "DATE TEXT, " +
                    "TIME TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(
            "DROP TABLE IF EXISTS $TABLE_NAME"
        )
        onCreate(db)
    }

    fun insertTask(model: ToDoModel) {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_2, model.task)
        contentValues.put(COL_3, model.status)
        contentValues.put(COL_4, model.date)
        contentValues.put(COL_5, model.time)

        db.insert(TABLE_NAME, null, contentValues)

    }

    fun updateTask(id: Int, task: String) {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_2, task)

        val rowsUpdated = db.update(
            TABLE_NAME,
            contentValues,
            "ID = ?",
            arrayOf(id.toString())
        )

        if (rowsUpdated != 1) {
            log.info("Ошибка количества обновлений. Больше 1")
        }
    }

    fun updateStatus(id: Int, status: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_3, status)

        val rowsUpdated = db.update(
            TABLE_NAME,
            contentValues,
            "ID = ?",
            arrayOf(id.toString())
        )

        if (rowsUpdated != 1) {
            log.info("Ошибка количества обновлений. Больше 1")
        }
    }

    fun updateTime(id: Int, time: String) {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_5, time)

        val rowsUpdated = db.update(
            TABLE_NAME,
            contentValues,
            "ID = ?",
            arrayOf(id.toString())
        )

        if (rowsUpdated != 1) {
            log.info("Ошибка количества обновлений. Больше 1")
        }
    }

    fun deleteTask(id: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(
            TABLE_NAME,
            "ID = ?", arrayOf(id.toString())
        )
    }

    fun getAllTasks(): List<ToDoModel> {
        val db: SQLiteDatabase = this.readableDatabase
        var cursor: Cursor? = null
        val modelList = mutableListOf<ToDoModel>()

        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(COL_1)
                val taskIndex = cursor.getColumnIndex(COL_2)
                val statusIndex = cursor.getColumnIndex(COL_3)
                val dateIndex = cursor.getColumnIndex(COL_4)
                val timeIndex = cursor.getColumnIndex(COL_5)

                do {
                    // Проверяем что индексы валидны
                    if (taskIndex >= 0) {
                        val toDoModel = ToDoModel(
                            task = cursor.getString(taskIndex),
                            id = if (idIndex >= 0) cursor.getInt(idIndex) else 0,
                            status = if (statusIndex >= 0) cursor.getInt(statusIndex) else 0,
                            date = if (dateIndex >= 0) cursor.getString(dateIndex) else null,
                            time = if (timeIndex >= 0) cursor.getString(timeIndex) else null
                        )
                        modelList.add(toDoModel)
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("DataBaseHelper", "Error reading database", e)
        } finally {
            cursor?.close()
        }
        return modelList
    }
}