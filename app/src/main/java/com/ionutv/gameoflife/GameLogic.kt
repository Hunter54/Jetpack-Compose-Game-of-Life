package com.ionutv.gameoflife

fun getNextScreen(input: Array<Array<Boolean>>): Array<Array<Boolean>> {
    val output = Array(gridsize){Array(gridsize) {false} }
    for (row in 0 until gridsize) {
        for (col in 0 until gridsize) {
            var neighbours = 0
            if (input[row - 1][col - 1]) ++neighbours
            if (input[row - 1][col]) ++neighbours
            if (input[row - 1][col + 1]) ++neighbours
            if (input[row] [col - 1]) ++neighbours
            if (input[row][col + 1]) ++neighbours
            if (input[row + 1] [col - 1]) ++neighbours
            if (input[row + 1] [col]) ++neighbours
            if (input[row + 1] [col + 1]) ++neighbours

            if (input[row][col] && (neighbours == 2 || neighbours == 3)) {
                output[row] [col] = true
            } else if (neighbours == 3) {
                output[row] [col] = true
            }
        }
    }
    return output
}