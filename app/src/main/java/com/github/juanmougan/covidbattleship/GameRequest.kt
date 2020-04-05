package com.github.juanmougan.covidbattleship

import com.google.gson.annotations.SerializedName

data class GameRequest(
    @SerializedName("player_name")
    val playerName: String,
    @SerializedName("board")
    val board: Array<Array<CellStatus>>
)
