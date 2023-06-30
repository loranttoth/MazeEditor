package com.loranttoth.mazeeditor

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.loranttoth.mazeeditor.data.Adapter
import com.loranttoth.mazeeditor.data.DatabaseHandler
import com.loranttoth.mazeeditor.data.Maze
import com.loranttoth.mazeeditor.data.MazeGenerator
import com.loranttoth.mazeeditor.tasks.HttpTask

class MainActivity : AppCompatActivity() {

    val TAG = "MAZEEDITOR"


    lateinit var lvlViewCustom: View
    lateinit var spLevel: Spinner

    lateinit var activity: AppCompatActivity

    lateinit var httpTask: HttpTask

    companion object {
        var arrayall: java.util.ArrayList<Maze> = ArrayList()
        var array: java.util.ArrayList<Maze> = ArrayList()
        lateinit var adapter: Adapter
        lateinit var rv: RecyclerView
        lateinit var activity: Activity

        public fun refreshMaze(maze: ByteArray, pos: Int) {
            array.get(pos).maze = maze
            adapter.notifyItemChanged(pos)
            updateMaze(array.get(pos))
        }

        public fun setMazeSize(pos: Int, row: Int, col: Int) {
            array.get(pos).y = row
            array.get(pos).x = col
            adapter.notifyItemChanged(pos)
            updateMaze(array.get(pos))
        }

        private fun loadMazeList(activity_: Activity) {
            activity = activity_

            MainActivity.arrayall = ArrayList()

            val databaseHandler = DatabaseHandler(activity)

            val mazeCount = databaseHandler.getMazesCount()

            //val mazeCount = 0

            if (mazeCount == 0) {
                databaseHandler.deleteAllMaze()
                createMazes(databaseHandler)
            }
            else {
                MainActivity.arrayall = databaseHandler.readMazes()
            }
            setAdapter(1, activity)
        }

        private fun createMazes(databaseHandler: DatabaseHandler) {
            var arrColRow = arrayOf(7, 7)
            var k = 0
            var maze: Maze? = null
            for (i in 1..120) {
                for (j in 1..20) {
                    //array.add(AdapterDataClass(j, colarr[i-1], rowarr[i-1], i, false))
                    arrColRow = getColRow(i)
                    val maze = MazeGenerator(arrColRow[0], arrColRow[1])
                    maze.Generate()
                    databaseHandler.createMaze(Maze(k++, j, arrColRow[0], arrColRow[1], i, maze.grid, false))
                }
            }
            MainActivity.arrayall = databaseHandler.readMazes()
        }

        private fun updateMaze(maze: Maze) {
            val databaseHandler = DatabaseHandler(activity)
            databaseHandler.updateMaze(maze)
        }

        private fun getColRow(i: Int): Array<Int> {
            var arr = arrayOf(9, 9)
            when(i) {
                in 1..10 -> arr = arrayOf(15,15)
                in 11..20 -> arr = arrayOf(18,18)
                in 21..30 -> arr = arrayOf(20,20)
                in 31..40 -> arr = arrayOf(22,22)
                in 41..50 -> arr = arrayOf(24,24)
                in 51..60 -> arr = arrayOf(27,27)
                in 61..70 -> arr = arrayOf(30,30)
                in 71..80 -> arr = arrayOf(33,33)
                in 81..90 -> arr = arrayOf(35,35)
                in 91..100 -> arr = arrayOf(39,39)
                in 101..110 -> arr = arrayOf(41,41)
                in 111..120 -> arr = arrayOf(43,43)
            }
            return arr
        }

        fun setAdapter(level: Int, activity: Activity) {
            array = arrayall.filter { it -> it.level == level } as java.util.ArrayList<Maze>
            adapter= Adapter(activity,MainActivity.array)
            //   rv.layoutManager = LinearLayoutManager(this)
            rv=activity.findViewById(R.id.rv)
            rv.adapter=adapter
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "init main activity")

        activity = this

        //httpTask = HttpTask(this)

        //httpTask.login()

        //val drawing = LevelView(this)
        setContentView(R.layout.activity_main)
        //setContentView(drawing)


        lvlViewCustom = layoutInflater.inflate(R.layout.level_view_custom_view, null)

        MainActivity.loadMazeList(this)

        spLevel = findViewById<Spinner>(R.id.spLevel)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, (1..120).toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spLevel.adapter = adapter
        spLevel.setSelection(0)
        spLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                MainActivity.setAdapter(position+1, activity)

            }

        }
    }


}