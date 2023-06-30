package com.loranttoth.mazeeditor.data

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF


enum class CellTypes {
    PATH, WALL, DOOR, CHEST, COIN
}

class MazeCell(var x1: Float, var y1: Float, var x2: Float, var y2: Float, var type1: Byte, var type2: Byte, var bitmap: Bitmap, var bitmap2: Bitmap?,
var isLeft: Boolean, var isTop: Boolean, var isRight: Boolean, var isBottom: Boolean) {

}