package com.github.juanmougan.covidbattleship

import com.google.gson.annotations.SerializedName
import java.util.*

data class GameStatusResponse(
    val id: UUID,
    val status: GameStatus,
    @SerializedName("next_player")
    val nextPlayer: Player
)
