package com.loranttoth.mazeeditor.data
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHandler(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase?) {
            //SQL - Structured Query Language
            var CREATE_MAZE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+
                    KEY_MAZE_LEVEL + " INT," +
                    KEY_MAZE_NUM + " INT," +
                    KEY_MAZE_ROWS + " INT," +
                    KEY_MAZE_COLS + " INT," +
                    KEY_MAZE_GRID + " BLOB" +");"

            db?.execSQL(CREATE_MAZE_TABLE)

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
            //create table again
            onCreate(db)
        }

        /**
         *  CRUD - Create, Read, Update, Delete
         */

        fun createMaze(maze: Maze) {
            var db: SQLiteDatabase = writableDatabase

            var values: ContentValues = ContentValues()
            values.put(KEY_MAZE_LEVEL, maze.level)
            values.put(KEY_MAZE_NUM, maze.index)
            values.put(KEY_MAZE_ROWS, maze.y)
            values.put(KEY_MAZE_COLS, maze.x)
            values.put(KEY_MAZE_GRID, maze.maze)

            var insert = db.insert(TABLE_NAME, null, values)

            Log.d("DATA INSERTED", "SUCCESS $insert")
            db.close()
        }

        fun readMaze(id: Int): Maze {
            var db: SQLiteDatabase = writableDatabase
            var cursor: Cursor = db.query(TABLE_NAME, arrayOf(KEY_ID,
                KEY_MAZE_LEVEL, KEY_MAZE_NUM,
                KEY_MAZE_ROWS, KEY_MAZE_COLS,
                KEY_MAZE_GRID), KEY_ID + "=?", arrayOf(id.toString()),
                null, null, null, null)


            if (cursor != null)
                cursor.moveToFirst()


            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val level = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_LEVEL))
            val index = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_NUM))
            val rows = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_ROWS))
            val cols = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_COLS))
            val grid = cursor.getBlob(cursor.getColumnIndex(KEY_MAZE_GRID))
            var maze = Maze(id, index, cols, rows, level, grid, true)



            return maze

        }

        fun readMazes(): ArrayList<Maze> {


            var db: SQLiteDatabase = readableDatabase
            var list: ArrayList<Maze> = ArrayList()

            //Select all chores from table
            var selectAll = "SELECT * FROM " + TABLE_NAME

            var cursor: Cursor = db.rawQuery(selectAll, null)

            //loop through our chores
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    val level = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_LEVEL))
                    val index = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_NUM))
                    val rows = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_ROWS))
                    val cols = cursor.getInt(cursor.getColumnIndex(KEY_MAZE_COLS))
                    val grid = cursor.getBlob(cursor.getColumnIndex(KEY_MAZE_GRID))
                    var maze = Maze(id, index, cols, rows, level, grid, true)

                    list.add(maze)

                }while (cursor.moveToNext())
            }
            return list
        }

        fun updateMaze(maze: Maze): Int {
            var db: SQLiteDatabase = writableDatabase

            var values: ContentValues = ContentValues()
            values.put(KEY_MAZE_LEVEL, maze.level)
            values.put(KEY_MAZE_NUM, maze.index)
            values.put(KEY_MAZE_ROWS, maze.y)
            values.put(KEY_MAZE_COLS, maze.x)
            values.put(KEY_MAZE_GRID, maze.maze)

            //update a row
            val code = db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(maze.id.toString()))
            db.close()
            return code
        }


        fun deleteMaze(id: Int) {
            var db: SQLiteDatabase = writableDatabase
            db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))
            db.close()
        }

        fun deleteAllMaze() {
            var db: SQLiteDatabase = writableDatabase
            db.delete(TABLE_NAME, KEY_ID + "<>?", arrayOf("-1"))
            db.close()
        }

        fun getMazesCount(): Int {
            var db: SQLiteDatabase = readableDatabase
            var countQuery = "SELECT * FROM " + TABLE_NAME
            var cursor: Cursor = db.rawQuery(countQuery, null)
            return cursor.count
        }
    }