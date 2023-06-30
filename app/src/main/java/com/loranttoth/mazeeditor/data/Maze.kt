package com.loranttoth.mazeeditor.data

import java.io.Serializable

data class Maze (var id: Int, var index: Int, var x: Int, var y: Int, var level: Int, var maze: ByteArray, var isSaved: Boolean): Serializable
