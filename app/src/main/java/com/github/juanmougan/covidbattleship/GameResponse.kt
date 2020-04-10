package com.github.juanmougan.covidbattleship

import com.google.gson.annotations.SerializedName
import java.util.*

data class GameResponse(
    val id: UUID,
    @SerializedName("shareable_link")
    val shareableLink: String,
    @SerializedName("player_1")
    val player1: Player,
    val status: GameStatus
)
