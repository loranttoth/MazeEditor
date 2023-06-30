package com.loranttoth.mazeeditor.data

import android.R.attr.x
import android.R.attr.y
import java.io.Serializable
import java.util.*


class MazeGenerator(var width: Int, var height: Int): Serializable{

        lateinit var grid: ByteArray

        var startX = 1
        var startY = 1

        val rnd = Random()

        public fun Generate() {
            grid = ByteArray(height*width) {0}

            startX = 1
            startY = 1

            grid[(startY*width)+startX] = 1

            MazeDigger(startY, startX)
            //makeObject(6)
            //makeObject(4)
            //makeObject(5)
            //makeObject(2)
        }

        public fun delAll() {
            grid = ByteArray(height*width) {1}
        }

        fun MazeDigger(y: Int, x: Int) {
            var directions = arrayOf(1, 2, 3, 4)

            directions = Tools.Shuffle(directions)

            for (i in 0.. directions.size-1) {
                if (directions[i] == 1) {
                    if (x - 2 <= 0)
                        continue
                    if (grid[(y*width)+(x - 2)].compareTo(0) == 0) {
                        grid[(y*width)+(x - 2)] = 1
                        grid[(y*width)+(x - 1)] = 1
                        MazeDigger(y, x - 2)
                    }
                }
                if (directions[i] == 2) {
                    if (y - 2 <= 0)
                        continue
                    if (grid[((y - 2)*width)+x].compareTo(0) == 0) {
                        grid[((y - 2)*width)+x] = 1
                        grid[((y - 1)*width)+x] = 1
                        MazeDigger(y - 2, x)
                    }
                }
                if (directions[i] == 3) {
                    if (y + 2 >= height - 1)
                        continue
                    if (grid[((y + 2)*width)+x].compareTo(0) == 0) {
                        grid[((y + 2)*width)+x] = 1
                        grid[((y + 1)*width)+x] = 1

                        MazeDigger(y + 2, x)
                    }
                }
                if (directions[i] == 4) {
                    if (x + 2 >= width - 1)
                        continue
                    if (grid[(y*width)+(x + 2)].compareTo(0) == 0) {
                        grid[(y*width)+(x + 2)] = 1
                        grid[(y*width)+(x + 1)] = 1

                        MazeDigger(y, x + 2)
                    }
                }
            }
        }

        fun makeObject(type: Byte) {
            var isok = false
            var y = 0
            var x = 0
            while(!isok) {
                y = rnd.nextInt(height)
                x = rnd.nextInt(width)
                if (x % 2 == 1 && y % 2 == 1) {
                    if (grid[(y * width) + x].compareTo(1) == 0) {
                        grid[(y * width) + x] = type
                        isok = true
                    }
                }
            }

        }

    public fun GetCell(y: Int, x: Int): Boolean {
            if (x >= width || x < 0 || y >= height || y <= 0) {
                return false
            }
            return grid[(y*width)+x].compareTo(1) == 0
        }
}