package com.loranttoth.mazeeditor.data

import java.util.*

class Tools {

    companion object {
        public fun Shuffle(array: Array<Int>): Array<Int> {
            val rnd = Random()
            var randomIndex = 1;
            for (j in 0..1000) {
                for (i in 0..array.size - 1) {
                    randomIndex = rnd.nextInt(array.size - i) + i;
                    var tempItem = array[randomIndex];

                    array[randomIndex] = array[i];
                    array[i] = tempItem;
                }
            }
            return array;
        }
    }

}