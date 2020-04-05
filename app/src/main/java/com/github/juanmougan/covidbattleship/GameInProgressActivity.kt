package com.github.juanmougan.covidbattleship

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class GameInProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_in_progress)
        val extras = intent.extras
        val gson = Gson()
        val (id, name, board, shots) = gson.fromJson(
            extras.getString(MainActivity.PLAYER_ONE_EXTRA),
            Player::class.java
        )
        Toast.makeText(this, "Game created by: $name", Toast.LENGTH_LONG).show()
    }
}
