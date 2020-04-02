package com.github.juanmougan.covidbattleship

import com.google.gson.annotations.SerializedName

class GameRequest {
    @SerializedName("player_name")
    private val playerName: String? = null

    @SerializedName("board")
    private val board: Array<Array<CellStatus>>? =
        Array(MainActivity.BOARD_ROW_NUMBER) {
            Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA }
        }
}
