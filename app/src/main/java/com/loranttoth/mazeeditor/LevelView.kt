package com.loranttoth.mazeeditor

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.loranttoth.mazeeditor.data.CellTypes
import com.loranttoth.mazeeditor.data.MazeCell
import java.util.*

class LevelView (context: Context, st: AttributeSet?) : View(context, st) {

    private lateinit var brush: Paint
    var w = 0
    var h = 0

    val images = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.wall),
        BitmapFactory.decodeResource(resources, R.drawable.dirt),
        BitmapFactory.decodeResource(resources, R.drawable.door1),
        BitmapFactory.decodeResource(resources, R.drawable.door2),
        BitmapFactory.decodeResource(resources, R.drawable.chest1),
        BitmapFactory.decodeResource(resources, R.drawable.key),
        BitmapFactory.decodeResource(resources, R.drawable.player),
    )

    lateinit var cells: Array<Array<MazeCell>>

    var cols: Int = 0
    var rows: Int = 0
    var cols2: Int = 0
    var rows2: Int = 0


    lateinit var maze: ByteArray

    var isEditor = false

    var aktTileId = 0

    var levelPos = 0

    lateinit var paintLine: Paint

    fun setMazeSize(col: Int, row: Int, maze_: ByteArray, isEditor_: Boolean) {
        cols = col
        rows = row
        rows2 = rows
        cols2 = cols
        rows = (rows - 1) / 2
        cols = (cols - 1) / 2
        maze = maze_
        isEditor = isEditor_
        w = width
        h = height

        paintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            strokeWidth = 11f
        }
        makeCells()
    }

    fun setLevelIndex(index: Int) {
        levelPos = index
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.w = w
        this.h = h
        makeCells()
    }

    fun makeCells() {
        val cells2 = Array(rows2) {Array(cols2) {MazeCell(0.0f,0.0f,0.0f,0.0f,0, -1, images[0], null, false, false, false, false)} }

        var path = CellTypes.PATH
        var rnd = Random()
        var nextInt: Byte = 0
        var nextInt2: Byte = 1

        for (i in 0 .. rows2-1) {
            for (j in 0..cols2 - 1) {
                nextInt = maze[(i*cols2)+j]
                nextInt2 = nextInt
                if (nextInt > 1) {
                    nextInt = 1
                }
                cells2[i][j] = MazeCell(0.0f, 0.0f, 0.0f, 0.0f, nextInt, nextInt2, images[nextInt.toInt()], images[nextInt2.toInt()], false, false, false, false)
                //cells[i][j] = MazeCell(x1, y1, x1+cellWH, y1+cellWH, 1, nextInt2, images[1], images[nextInt2.toInt()])

            }

        }


        //val cH = h / (if (isEditor) rows else 20)
        //val cW = w / (if (isEditor) cols else 20)
        val cH = h / rows
        val cW = w / cols
        val cellWH = Math.min(cH, cW)

        val x0 = ((w-(cellWH * cols)) / 2).toFloat()
        var x1 = x0
        var y1 = 0.0f

        cells = Array(rows) {Array(cols) {MazeCell(0.0f,0.0f,0.0f,0.0f,0, -1, images[0], null, false, false, false, false)} }
        nextInt = 1
        for (i in 0 .. rows-1) {
            for (j in 0..cols - 1) {
                cells[i][j] = MazeCell(x1, y1, x1+cellWH, y1+cellWH, nextInt, 1, images[nextInt.toInt()], images[nextInt.toInt()], false, false, false, false)
                //cells[i][j] = MazeCell(x1, y1, x1+cellWH, y1+cellWH, 1, nextInt2, images[1], images[nextInt2.toInt()])
                if (i == 0)
                    cells[i][j].isTop = true
                if (i == rows-1)
                        cells[i][j].isBottom = true
                if (j == 0)
                    cells[i][j].isLeft = true
                if (j == cols-1)
                    cells[i][j].isRight = true

                x1+=cellWH
            }
            x1 = x0
            y1+=cellWH
        }

        for (i in 0 .. rows2 - 2) {
            for (j in 0..cols2 - 2) {
                if (cells2[i][j].type1.compareTo(0) == 0) {
                    if (i > 0 && j > 1 && i % 2 == 1 && j % 2 == 0)
                        cells[i/2][(j/2)-1].isRight = true;
                    if (i > 0 && j > 0 && i % 2 == 0 && j % 2 ==1)
                        cells[(i/2)-1][(j/2)].isBottom = true;
                    //if (i > 1 && j > 1 && i % 2 == 0 && j % 2 == 1)
                    //    cells[i/2][(j/2)].isBottom = true;
                   // if (i > 1 && i % 2 == 1 && j % 2 == 1)
                   //     cells[(i/2)-1][(j/2)].isRight = true;
                }
                if (cells2[i][j].type2.compareTo(0) !== 0) {
                    cells[(i/2)][(j/2)].type2 = cells2[i][j].type2
                    cells[(i/2)][(j/2)].bitmap2 = images[cells2[i][j].type2.toInt()]
                }
            }
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        brush = Paint(Paint.ANTI_ALIAS_FLAG)
        brush.color = Color.parseColor("green")

        //canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(), 24f, brush)
        for (i in 0 .. rows-1) {
            for (j in 0 .. cols-1) {
                drawCell(cells[i][j], canvas)
            }
        }
        for (i in 0 .. rows-1) {
            for (j in 0 .. cols-1) {
                drawLine(cells[i][j], canvas)
            }
        }
        canvas.save()
        super.onDraw(canvas)
    }

    fun drawCell(cell: MazeCell, canvas: Canvas) {
        canvas.drawBitmap(cell.bitmap, null, RectF(cell.x1, cell. y1, cell. x2, cell.y2), null)
        if (cell.bitmap2 != null && !cell.type1.equals(cell.type2))
            canvas.drawBitmap(cell.bitmap2!!, null, RectF(cell.x1, cell.y1, cell.x2, cell.y2), null)

    }

    fun drawLine(cell: MazeCell, canvas: Canvas) {
        if (cell.isTop)
            canvas.drawLine(cell.x1, cell.y1, cell.x2, cell.y1, paintLine)
        if (cell.isLeft)
            canvas.drawLine(cell.x1, cell.y1, cell.x1, cell.y2, paintLine)
        if (cell.isBottom)
            canvas.drawLine(cell.x1, cell.y2, cell.x2, cell.y2, paintLine)
        if (cell.isRight)
            canvas.drawLine(cell.x2, cell.y1, cell.x2, cell.y2, paintLine)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when ever there is a touch on the screen,
        // we can get the position of touch
        // which we may use it for tracking some of the game objects
        if (isEditor) {
            val tX = event.x.toInt()
            val tY = event.y.toInt()

            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> setTile(tX, tY)
                MotionEvent.ACTION_MOVE -> if (aktTileId != 5) setTile(tX, tY)
                MotionEvent.ACTION_UP -> {}
                MotionEvent.ACTION_CANCEL -> {}
                MotionEvent.ACTION_OUTSIDE -> {}
            }
        }
        return isEditor
    }

    fun setTile(tX: Int, tY: Int) {
        var i2 = 0
        var j2 = 0

        for (i in 0 .. rows-1) {
            for (j in 0 .. cols-1) {
                if (tX > cells[i][j].x1 &&
                    tX < cells[i][j].x2 &&
                    tY > cells[i][j].y1 &&
                    tY < cells[i][j].y2) {
                        if (aktTileId > 1) {
                            if (cells[i][j].type1.compareTo(1) == 0) {
                                if (aktTileId == 6) {
                                    if (cells[i][j].type1.compareTo(1) == 0) {
                                        for (i1 in 0..rows - 1) {
                                            for (j1 in 0..cols - 1) {
                                                if (cells[i1][j1].type2.compareTo(6) == 0) {
                                                    cells[i1][j1].type2 = 1
                                                    cells[i1][j1].bitmap2 = images[1]
                                                }
                                            }
                                        }
                                    } else
                                        return;
                                }

                                cells[i][j].type2 = aktTileId.toByte()
                                cells[i][j].bitmap2 = images[aktTileId]
                                i2 = (i*2)+1
                                j2 = (j*2)+1
                                maze[(i2 * cols2) + j2] = aktTileId.toByte()
                                MainActivity.refreshMaze(maze, levelPos)
                            }
                        }
                        else {
                            cells[i][j].type1 = aktTileId.toByte()
                            cells[i][j].bitmap = images[aktTileId]
                            i2 = (i*2)+1
                            j2 = (j*2)+1
                            maze[(i2*cols2)+j2] = aktTileId.toByte()

                            cells[i][j].type2 = aktTileId.toByte()
                            cells[i][j].bitmap2 = images[aktTileId]
                            MainActivity.refreshMaze(maze, levelPos)

                        }
                        invalidate()
                        return
                }
            }
        }

    }

    fun setAktTile(tile: Int) {
        aktTileId = tile

    }
}