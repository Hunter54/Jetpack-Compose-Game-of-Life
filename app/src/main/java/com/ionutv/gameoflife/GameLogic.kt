package com.ionutv.gameoflife

import java.lang.IndexOutOfBoundsException

fun getNextScreen(gridSize : Int, input: Array<Array<Boolean>>): Array<Array<Boolean>> {
    val output = Array(gridSize){Array(gridSize) {false} }
    for (row in 0 until gridSize) {
        for (col in 0 until gridSize) {
            var neighbours = 0
            for(x in -1..1){
                for(y in -1..1){
                    try {
                        if(input[row + x][col + y]){
                            if(x !=0 || y !=0)
                                ++neighbours
                        }
                    }
                    catch (e : IndexOutOfBoundsException){
                        continue
                    }

                }
            }
            if (input[row][col] && (neighbours == 2 || neighbours == 3)) {
                output[row] [col] = true
            } else if (neighbours == 3) {
                output[row] [col] = true
            }
        }
    }
    return output
}