package com.github.juanmougan.covidbattleship

import java.util.*

data class Player(
    val id: UUID,
    val name: String,
    val board: Array<Array<String>>? =
        Array(MainActivity.BOARD_ROW_NUMBER) {
            Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA.name }
        },
    val shots: Array<Array<String>>? =
        Array(MainActivity.BOARD_ROW_NUMBER) {
            Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA.name }
        }
)
