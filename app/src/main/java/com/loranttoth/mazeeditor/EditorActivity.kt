package com.loranttoth.mazeeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import com.loranttoth.mazeeditor.data.Maze
import com.loranttoth.mazeeditor.data.EXTRA_MESSAGE
import com.loranttoth.mazeeditor.data.EXTRA_POSITION
import com.loranttoth.mazeeditor.data.MazeGenerator

class EditorActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var levelView: LevelView

    lateinit var spRows: Spinner
    lateinit var spCols: Spinner

    lateinit var ivWall: ImageView
    lateinit var ivPath: ImageView
    lateinit var ivChest: ImageView
    lateinit var ivCoin: ImageView
    lateinit var ivDoor: ImageView
    lateinit var ivDoor2: ImageView
    lateinit var ivPlayer: ImageView

    var levelPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        var data = intent.getSerializableExtra(EXTRA_MESSAGE) as Maze

        levelPosition = intent.getIntExtra(EXTRA_POSITION, 0)

        levelView = findViewById<LevelView>(R.id.levelview)
        levelView.setMazeSize(data.x, data.y, data.maze, true)
        levelView.setLevelIndex(levelPosition)

        ivWall = findViewById<ImageView>(R.id.imWall)
        ivWall.setOnClickListener(this)

        ivPath = findViewById<ImageView>(R.id.imDirt)
        ivPath.setOnClickListener(this)

        ivChest = findViewById<ImageView>(R.id.imChest)
        ivChest.setOnClickListener(this)

        ivCoin = findViewById<ImageView>(R.id.imCoin)
        ivCoin.setOnClickListener(this)

        ivDoor = findViewById<ImageView>(R.id.imDoor)
        ivDoor.setOnClickListener(this)
        ivDoor2 = findViewById<ImageView>(R.id.imDoor2)
        ivDoor2.setOnClickListener(this)

        ivPlayer = findViewById<ImageView>(R.id.imPlayer)
        ivPlayer.setOnClickListener(this)

        val btnReset = findViewById<Button>(R.id.btnReset)
        btnReset.setOnClickListener(this)

        val btnDelAll = findViewById<Button>(R.id.btnDelAll)
        btnDelAll.setOnClickListener(this)

        spRows = findViewById<Spinner>(R.id.spRows)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("15", "18", "20", "22", "24", "27", "30", "33", "35", "39", "41", "43", "45", "47", "49","51"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spRows.adapter = adapter
        val posY = adapter.getPosition(data.y.toString())
        spRows.setSelection(posY)


        spCols = findViewById<Spinner>(R.id.spCols)
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("15", "18", "20", "22", "24", "27", "30", "33", "35", "39", "41", "43", "45", "47", "49","51"))
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCols.adapter = adapter1
        val posX = adapter1.getPosition(data.x.toString())
        spCols.setSelection(posX)
    }

    fun setIconBorder(id: Int) {
        ivWall.setBackgroundResource(if (id == 0) R.drawable.image_border else R.drawable.image_border_null)
        ivPath.setBackgroundResource(if (id == 1) R.drawable.image_border else R.drawable.image_border_null)
        ivDoor.setBackgroundResource(if (id == 2) R.drawable.image_border else R.drawable.image_border_null)
        ivDoor2.setBackgroundResource(if (id == 3) R.drawable.image_border else R.drawable.image_border_null)
        ivChest.setBackgroundResource(if (id == 4) R.drawable.image_border else R.drawable.image_border_null)
        ivCoin.setBackgroundResource(if (id == 5) R.drawable.image_border else R.drawable.image_border_null)
        ivPlayer.setBackgroundResource(if (id == 6) R.drawable.image_border else R.drawable.image_border_null)
    }

    override fun onClick(v: View?) {
        var id = -1
        val str = v!!.tag
        when (str) {
            "imWall" -> id = 0
            "imDirt" -> id = 1
            "imDoor" -> id = 2
            "imDoor2" -> id = 3
            "imChest" -> id = 4
            "imCoin" -> id = 5
            "imPlayer" -> id = 6
            "btnReset" -> makeNewMaze()
            "btnDelAll" -> deleteMaze()
        }
        if (id > -1)
            setIconBorder(id)

        levelView.setAktTile(id)
    }

    fun makeNewMaze() {
        val row = spRows.getSelectedItem().toString().toInt()
        val col = spCols.getSelectedItem().toString().toInt()

        val maze = MazeGenerator(col, row)
        maze.Generate()

        levelView.setMazeSize(col, row, maze.grid, true)

        MainActivity.refreshMaze(maze.grid, levelPosition)
        MainActivity.setMazeSize(levelPosition, row, col)

    }

    fun deleteMaze() {
        val row = spRows.getSelectedItem().toString().toInt()
        val col = spCols.getSelectedItem().toString().toInt()

        val maze = MazeGenerator(col, row)
        maze.delAll()
        levelView.setMazeSize(col, row, maze.grid, true)
        MainActivity.refreshMaze(maze.grid, levelPosition)
        MainActivity.setMazeSize(levelPosition, row, col)


    }
}